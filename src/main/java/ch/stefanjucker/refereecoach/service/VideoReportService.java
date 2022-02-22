package ch.stefanjucker.refereecoach.service;

import ch.stefanjucker.refereecoach.configuration.RefereeCoachProperties;
import ch.stefanjucker.refereecoach.domain.User;
import ch.stefanjucker.refereecoach.domain.VideoComment;
import ch.stefanjucker.refereecoach.domain.VideoCommentReply;
import ch.stefanjucker.refereecoach.domain.VideoReport;
import ch.stefanjucker.refereecoach.domain.repository.VideoCommentReplyRepository;
import ch.stefanjucker.refereecoach.domain.repository.VideoCommentRepository;
import ch.stefanjucker.refereecoach.domain.repository.VideoReportRepository;
import ch.stefanjucker.refereecoach.dto.CreateRepliesDTO;
import ch.stefanjucker.refereecoach.dto.Reportee;
import ch.stefanjucker.refereecoach.dto.VideoCommentDTO;
import ch.stefanjucker.refereecoach.dto.VideoReportDTO;
import ch.stefanjucker.refereecoach.dto.VideoReportDiscussionDTO;
import ch.stefanjucker.refereecoach.mapper.DTOMapper;
import ch.stefanjucker.refereecoach.service.BasketplanService.Federation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toCollection;
import static org.springframework.data.domain.Sort.Order.desc;
import static org.springframework.data.domain.Sort.by;

@Slf4j
@Service
public class VideoReportService {

    private static final DTOMapper DTO_MAPPER = DTOMapper.INSTANCE;

    private final VideoReportRepository videoReportRepository;
    private final VideoCommentRepository videoCommentRepository;
    private final VideoCommentReplyRepository videoCommentReplyRepository;
    private final BasketplanService basketplanService;
    private final JavaMailSender mailSender;
    private final RefereeCoachProperties properties;
    private final Environment environment;

    public VideoReportService(VideoReportRepository videoReportRepository,
                              VideoCommentRepository videoCommentRepository,
                              VideoCommentReplyRepository videoCommentReplyRepository,
                              BasketplanService basketplanService,
                              JavaMailSender mailSender,
                              RefereeCoachProperties properties,
                              Environment environment) {
        this.videoReportRepository = videoReportRepository;
        this.videoCommentRepository = videoCommentRepository;
        this.videoCommentReplyRepository = videoCommentReplyRepository;
        this.basketplanService = basketplanService;
        this.mailSender = mailSender;
        this.properties = properties;
        this.environment = environment;
    }

    public VideoReportDTO create(Federation federation, String gameNumber, String youtubeId, Reportee reportee, User user) {
        var game = basketplanService.findGameByNumber(federation, gameNumber).orElseThrow();

        var videoReport = new VideoReport();
        videoReport.setId(getUuid());
        var basketplanGame = DTO_MAPPER.fromDTO(game);
        basketplanGame.setYoutubeId(youtubeId); // either coming from Basketplan or manually entered by user
        videoReport.setBasketplanGame(basketplanGame);
        videoReport.setReportee(reportee);
        videoReport.setReporter(user);
        videoReport.setFinished(false);

        return DTO_MAPPER.toDTO(videoReportRepository.save(videoReport));
    }

    public VideoReportDTO copy(String sourceId, Reportee reportee, User user) {
        var source = videoReportRepository.findById(sourceId).orElseThrow();

        var copy = DTO_MAPPER.copy(source);
        copy.setId(getUuid());
        copy.setReporter(user);
        copy.setReportee(reportee);
        copy.setFinished(false);
        var newVideoReport = videoReportRepository.save(copy);

        List<VideoCommentDTO> newComments = new ArrayList<>();
        for (var videoComment : videoCommentRepository.findByVideoReportId(sourceId)) {
            var commentCopy = DTO_MAPPER.copy(videoComment);
            commentCopy.setVideoReportId(newVideoReport.getId());
            newComments.add(DTO_MAPPER.toDTO(videoCommentRepository.save(commentCopy)));
        }

        return DTO_MAPPER.toDTO(newVideoReport, newComments);
    }

    private String getUuid() {
        String uuid;
        do {
            uuid = RandomStringUtils.randomAlphabetic(10);
        } while (videoReportRepository.existsById(uuid));

        return uuid;
    }

    @Transactional
    public VideoReportDTO update(String id, VideoReportDTO dto, User user) {
        var videoReport = videoReportRepository.findById(id).orElseThrow();
        if (!videoReport.getReporter().getEmail().equals(user.getEmail())) {
            log.error("user {} tried to update video-report {} that does not belong to them", user, videoReport);
            throw new IllegalStateException("user is not allowed to update this video-report!");
        }

        if (videoReport.isFinished()) {
            log.error("user {} tried to update video-report {} that is already finished", user, videoReport);
            throw new IllegalStateException("user is not allowed to update already finished video-report!");
        }

        DTO_MAPPER.update(dto, videoReport);
        videoReport = videoReportRepository.save(videoReport);
        Set<Long> existingVideoCommentIds = videoCommentRepository.findByVideoReportId(id).stream()
                                                                  .map(VideoComment::getId)
                                                                  .collect(toCollection(HashSet::new));

        List<VideoCommentDTO> comments = new ArrayList<>();
        for (var videoCommentDTO : dto.videoComments()) {
            var videoComment = videoCommentRepository.save(DTO_MAPPER.fromDTO(videoCommentDTO, dto.id()));
            comments.add(DTO_MAPPER.toDTO(videoComment));
            existingVideoCommentIds.remove(videoComment.getId());
        }
        videoCommentRepository.deleteAllById(existingVideoCommentIds);

        if (videoReport.isFinished()) {
            SimpleMailMessage simpleMessage = new SimpleMailMessage();
            try {
                var referee = videoReport.relevantReferee();
                log.info("finishing {}, send email to {}", videoReport, referee.getEmail());

                simpleMessage.setSubject("[Referee Coach] New Video Report");
                simpleMessage.setFrom(environment.getRequiredProperty("spring.mail.username"));
                simpleMessage.setReplyTo(videoReport.getReporter().getEmail());
                simpleMessage.setBcc("stefan.jucker@gmail.com");

                if (properties.isOverrideRecipient()) {
                    simpleMessage.setTo(videoReport.getReporter().getEmail());
                    simpleMessage.setSubject(simpleMessage.getSubject() + " (%s)".formatted(referee.getEmail()));
                } else {
                    simpleMessage.setTo(referee.getEmail());
                    // make sure Fabrizio does not receive mail twice when he is the reporter
                    simpleMessage.setCc(Stream.of(videoReport.getReporter().getEmail(), "fabrizio.pizio@swissbasketball.ch")
                                              .distinct()
                                              .toArray(String[]::new));
                }

                simpleMessage.setText(("Hi %s%n%nYour video report is ready.%nPlease visit: %s/#/view/%s" +
                        "%nFor discussion of the comments, use the following: %s/#/discuss/%s").formatted(referee.getName(),
                                                                                                          properties.getBaseUrl(),
                                                                                                          dto.id(),
                                                                                                          properties.getBaseUrl(),
                                                                                                          dto.id()));

                mailSender.send(simpleMessage);
            } catch (MailException e) {
                log.error("could not send email to: " + Arrays.toString(simpleMessage.getTo()), e);
            }
        }
        return DTO_MAPPER.toDTO(videoReport, comments);
    }

    public Optional<VideoReportDTO> find(String id) {

        List<VideoCommentDTO> videoCommentDTOs = new ArrayList<>();
        for (var videoComment : videoCommentRepository.findByVideoReportId(id)) {
            var replies = videoCommentReplyRepository.findByVideoCommentIdOrderByRepliedAt(videoComment.getId());
            videoCommentDTOs.add(DTO_MAPPER.toDTO(videoComment, DTO_MAPPER.toDTO(replies)));
        }

        return videoReportRepository.findById(id)
                                    .map(videoReport -> DTO_MAPPER.toDTO(videoReport, videoCommentDTOs));
    }

    public void delete(String id, User user) {
        // verify, that us is allowed to delete this video report
        var videoReport = videoReportRepository.findById(id).orElseThrow();
        if (user.isAdmin() || isUnfinishedReportOwnedByUser(videoReport, user)) {
            videoReportRepository.delete(videoReport);
        } else {
            log.error("user ({}) tried to delete video report ({}), but is not authorized to do so", user, videoReport);
            throw new IllegalStateException("user is not allowed to delete this video-report!");
        }
    }

    public VideoReportDiscussionDTO getVideoReportDiscussion(String videoReportId) {
        var videoReport = videoReportRepository.findById(videoReportId).orElseThrow();

        List<VideoCommentDTO> videoCommentDTOs = new ArrayList<>();
        for (var videoComment : videoCommentRepository.findVideoCommentsByGameNumberAndReporter(videoReport.getBasketplanGame()
                                                                                                           .getGameNumber(), videoReport.getReporter()
                                                                                                                                        .getId())) {
            var replies = videoCommentReplyRepository.findByVideoCommentIdOrderByRepliedAt(videoComment.getId());
            videoCommentDTOs.add(DTO_MAPPER.toDTO(videoComment, DTO_MAPPER.toDTO(replies)));
        }

        return new VideoReportDiscussionDTO(videoReport.getId(),
                                            DTO_MAPPER.toDTO(videoReport.getBasketplanGame()),
                                            DTO_MAPPER.toDTO(videoReport.getReporter()),
                                            videoReport.relevantReferee().getName(),
                                            videoCommentDTOs);
    }

    private boolean isUnfinishedReportOwnedByUser(VideoReport videoReport, User user) {
        return !videoReport.isFinished() && videoReport.getReporter().getEmail().equals(user.getEmail());
    }

    public List<VideoReportDTO> findAll() {
        return videoReportRepository.findAll(by(desc("basketplanGame.date"),
                                                desc("basketplanGame.gameNumber"),
                                                desc("reportee")))
                                    .stream()
                                    .map(DTO_MAPPER::toDTO) // no need to fill the comments as well, not needed in report overview
                                    .toList();
    }

    public void addReplies(User user, String id, CreateRepliesDTO dto) {
        var videoReport = videoReportRepository.findById(id).orElseThrow();

        String repliedBy;
        if (user != null) {
            repliedBy = user.getName();
        } else {
            repliedBy = videoReport.relevantReferee().getName();
        }

        for (var reply : dto.replies()) {
            videoCommentReplyRepository.save(new VideoCommentReply(null, repliedBy, LocalDateTime.now(), reply.comment(), reply.commentId()));
        }

        for (var report : videoReportRepository.findByBasketplanGameGameNumberAndReporterId(videoReport.getBasketplanGame().getGameNumber(),
                                                                                            videoReport.getReporter().getId())) {

            if (user != null || !report.getId().equals(id)) {
                sendDiscussionEmail(repliedBy, report.relevantReferee().getEmail(), report.getId());
            }
        }

        if (user == null) {
            sendDiscussionEmail(repliedBy, videoReport.getReporter().getEmail(), videoReport.getId());
        }
    }

    private void sendDiscussionEmail(String repliedBy, String recipient, String reportId) {
        SimpleMailMessage simpleMessage = new SimpleMailMessage();
        try {
            simpleMessage.setSubject("[Referee Coach] New Video Report Discussion");
            simpleMessage.setFrom(environment.getRequiredProperty("spring.mail.username"));
            simpleMessage.setBcc("stefan.jucker@gmail.com");

            if (properties.isOverrideRecipient()) {
                simpleMessage.setTo("stefan.jucker@gmail.com");
                simpleMessage.setSubject(simpleMessage.getSubject() + " (%s)".formatted(recipient));
            } else {
                simpleMessage.setTo(recipient);
            }
            simpleMessage.setText(("Hi%n%s added new replies for a video report.%nPlease visit: %s/#/discuss/%s").formatted(
                    repliedBy,
                    properties.getBaseUrl(),
                    reportId));

        } catch (MailException e) {
            log.error("could not send email to: " + Arrays.toString(simpleMessage.getTo()), e);
        }
    }
}
