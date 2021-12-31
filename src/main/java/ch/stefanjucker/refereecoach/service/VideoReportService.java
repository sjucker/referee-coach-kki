package ch.stefanjucker.refereecoach.service;

import ch.stefanjucker.refereecoach.configuration.RefereeCoachProperties;
import ch.stefanjucker.refereecoach.domain.User;
import ch.stefanjucker.refereecoach.domain.VideoReport;
import ch.stefanjucker.refereecoach.domain.repository.VideoReportRepository;
import ch.stefanjucker.refereecoach.dto.Reportee;
import ch.stefanjucker.refereecoach.dto.VideoReportDTO;
import ch.stefanjucker.refereecoach.mapper.DTOMapper;
import ch.stefanjucker.refereecoach.service.BasketplanService.Federation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.Sort.Order.desc;
import static org.springframework.data.domain.Sort.by;

@Slf4j
@Service
public class VideoReportService {

    private static final DTOMapper DTO_MAPPER = DTOMapper.INSTANCE;

    private final VideoReportRepository videoReportRepository;
    private final BasketplanService basketplanService;
    private final JavaMailSender mailSender;
    private final RefereeCoachProperties properties;
    private final Environment environment;

    public VideoReportService(VideoReportRepository videoReportRepository,
                              BasketplanService basketplanService,
                              JavaMailSender mailSender,
                              RefereeCoachProperties properties,
                              Environment environment) {
        this.videoReportRepository = videoReportRepository;
        this.basketplanService = basketplanService;
        this.mailSender = mailSender;
        this.properties = properties;
        this.environment = environment;
    }

    public VideoReportDTO create(Federation federation, String gameNumber, Reportee reportee, User user) {
        var game = basketplanService.findGameByNumber(federation, gameNumber).orElseThrow();

        var videoReport = new VideoReport();
        videoReport.setId(getUuid());
        videoReport.setBasketplanGame(DTO_MAPPER.fromDTO(game));
        videoReport.setReportee(reportee);
        videoReport.setReporter(user);
        videoReport.setVideoComments(new ArrayList<>());
        videoReport.setFinished(false);

        videoReport = videoReportRepository.save(videoReport);

        return DTO_MAPPER.toDTO(videoReport);
    }

    private String getUuid() {
        String uuid;
        do {
            uuid = RandomStringUtils.randomAlphabetic(10);
        } while (videoReportRepository.existsById(uuid));

        return uuid;
    }

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

        if (videoReport.isFinished()) {
            SimpleMailMessage simpleMessage = new SimpleMailMessage();
            try {
                var referee = videoReport.relevantReferee();
                log.info("finishing {}, send email to {}", videoReport, referee.getEmail());

                simpleMessage.setSubject("[Referee Coach] New Video Report");
                simpleMessage.setFrom(environment.getRequiredProperty("spring.mail.username"));
                simpleMessage.setReplyTo(videoReport.getReporter().getEmail());

                if (properties.isOverrideRecipient()) {
                    simpleMessage.setTo(videoReport.getReporter().getEmail());
                    simpleMessage.setSubject(simpleMessage.getSubject() + " (%s)".formatted(referee.getEmail()));
                } else {
                    simpleMessage.setTo(referee.getEmail());
                    simpleMessage.setCc(videoReport.getReporter().getEmail());
                }

                simpleMessage.setText("Hi %s\nPlease visit: %s/#/view/%s".formatted(referee.getName(),
                                                                                    properties.getBaseUrl(),
                                                                                    dto.id()));

                mailSender.send(simpleMessage);
            } catch (MailException e) {
                log.error("could not send email to: " + Arrays.toString(simpleMessage.getTo()), e);
            }
        }
        return dto;
    }

    public Optional<VideoReportDTO> find(String id) {
        return videoReportRepository.findById(id)
                                    .map(DTO_MAPPER::toDTO);
    }

    public List<VideoReportDTO> findAll() {
        return videoReportRepository.findAll(by(desc("basketplanGame.date")))
                                    .stream()
                                    .map(DTO_MAPPER::toDTO)
                                    .toList();
    }

}
