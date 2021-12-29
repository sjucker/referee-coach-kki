package ch.stefanjucker.refereecoach.service;

import ch.stefanjucker.refereecoach.domain.VideoReport;
import ch.stefanjucker.refereecoach.domain.repository.UserRepository;
import ch.stefanjucker.refereecoach.domain.repository.VideoReportRepository;
import ch.stefanjucker.refereecoach.dto.Reportee;
import ch.stefanjucker.refereecoach.dto.VideoReportDTO;
import ch.stefanjucker.refereecoach.mapper.DTOMapper;
import ch.stefanjucker.refereecoach.service.BasketplanService.Federation;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VideoReportService {

    private static final DTOMapper DTO_MAPPER = DTOMapper.INSTANCE;

    private final VideoReportRepository videoReportRepository;
    private final UserRepository userRepository;
    private final BasketplanService basketplanService;
    private final JavaMailSender mailSender;

    public VideoReportService(VideoReportRepository videoReportRepository,
                              UserRepository userRepository,
                              BasketplanService basketplanService,
                              JavaMailSender mailSender) {
        this.videoReportRepository = videoReportRepository;
        this.userRepository = userRepository;
        this.basketplanService = basketplanService;
        this.mailSender = mailSender;
    }

    public VideoReportDTO create(Federation federation, String gameNumber, Reportee reportee) {
        // TODO proper error handling
        var game = basketplanService.findGameByNumber(federation, gameNumber).orElseThrow();

        var videoReport = new VideoReport();
        videoReport.setId(getUuid());
        videoReport.setBasketplanGame(DTO_MAPPER.fromDTO(game));
        videoReport.setReportee(reportee);
        videoReport.setReporter(userRepository.findAll().get(0)); // TODO actual user!
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

    public VideoReportDTO update(String id, VideoReportDTO dto) {
        var videoReport = videoReportRepository.findById(id).orElseThrow();
        DTO_MAPPER.update(dto, videoReport);
        videoReport = videoReportRepository.save(videoReport);

        if (videoReport.isFinished()) {
            // TODO log it
            SimpleMailMessage simpleMessage = new SimpleMailMessage();
            simpleMessage.setFrom("noreply@stefanjucker.ch"); // TODO add to properties
            simpleMessage.setTo("stefan.jucker@gmail.com"); // TODO actual referee
            simpleMessage.setSubject("New Video Report");
            // TODO error handling
            var referee = videoReport.relevantReferee();
            // TODO do not hardcode base URL
            simpleMessage.setText("Hi %s (%s)\nPlease visit: https://referee-coach.herokuapp.com/#/view/%s"
                                          .formatted(referee.getName(), referee.getEmail(), dto.id()));

            mailSender.send(simpleMessage);
        }
        return dto;
    }

    public Optional<VideoReportDTO> find(String id) {
        return videoReportRepository.findById(id)
                                    .map(DTO_MAPPER::toDTO);
    }

    public List<VideoReportDTO> findAll() {
        // TODO sort it
        return videoReportRepository.findAll().stream()
                                    .map(DTO_MAPPER::toDTO)
                                    .toList();
    }

}
