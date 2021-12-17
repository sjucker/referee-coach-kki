package ch.stefanjucker.refereecoach.service.expertise;

import ch.stefanjucker.refereecoach.dto.Reportee;
import ch.stefanjucker.refereecoach.dto.VideoExpertiseDTO;
import ch.stefanjucker.refereecoach.service.basketplan.BasketplanService;
import ch.stefanjucker.refereecoach.service.basketplan.BasketplanService.Federation;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class VideoExpertiseService {

    // TODO move this to database

    private final Map<String, VideoExpertiseDTO> expertisen = new HashMap<>();

    private final BasketplanService basketplanService;
    private final JavaMailSender mailSender;

    public VideoExpertiseService(BasketplanService basketplanService, JavaMailSender mailSender) {
        this.basketplanService = basketplanService;
        this.mailSender = mailSender;
    }

    public VideoExpertiseDTO create(Federation federation, String gameNumber, Reportee reportee) {
        SimpleMailMessage simpleMessage = new SimpleMailMessage();
        simpleMessage.setFrom("logging@stefanjucker.ch");
        simpleMessage.setTo("jucker@run4fun.ch");
        simpleMessage.setSubject("test");
        simpleMessage.setText("test");

        mailSender.send(simpleMessage);

        // TODO proper error handling
        var game = basketplanService.findGameByNumber(federation, gameNumber).orElseThrow();

        var uuid = getUuid();

        var dto = new VideoExpertiseDTO(
                uuid,
                game,
                reportee,
                "",
                "",
                "",
                "",
                "",
                "",
                List.of(),
                false
        );
        expertisen.put(uuid, dto);
        return dto;
    }

    private String getUuid() {
        String uuid;
        do {
            uuid = RandomStringUtils.randomAlphabetic(10);
        } while (expertisen.containsKey(uuid));

        return uuid;
    }

    public VideoExpertiseDTO update(String id, VideoExpertiseDTO dto) {
        expertisen.put(id, dto);
        return dto;
    }

    public Optional<VideoExpertiseDTO> find(String id) {
        return Optional.ofNullable(expertisen.get(id));
    }

    public List<VideoExpertiseDTO> findAll() {
        return new ArrayList<>(expertisen.values());
    }

}
