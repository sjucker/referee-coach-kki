package ch.stefanjucker.refereecoach.rest.expertise;

import ch.stefanjucker.refereecoach.dto.CreateVideoExpertiseDTO;
import ch.stefanjucker.refereecoach.dto.VideoExpertiseDTO;
import ch.stefanjucker.refereecoach.service.expertise.VideoExpertiseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class VideoExpertiseResource {

    private static final Logger logger = LoggerFactory.getLogger(VideoExpertiseResource.class);

    private final VideoExpertiseService expertiseService;

    public VideoExpertiseResource(VideoExpertiseService expertiseService) {
        this.expertiseService = expertiseService;
    }

    @GetMapping(value = "/video-expertise")
    public ResponseEntity<List<VideoExpertiseDTO>> getAllExpertise() {
        logger.info("GET /video-expertise");

        return ResponseEntity.ok(expertiseService.findAll());
    }

    @GetMapping(value = "/video-expertise/{id}")
    public ResponseEntity<VideoExpertiseDTO> getExpertise(@PathVariable String id) {
        logger.info("GET /video-expertise/{}", id);

        return expertiseService.find(id)
                               .map(ResponseEntity::ok)
                               .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/video-expertise", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<VideoExpertiseDTO> createExpertise(@RequestBody CreateVideoExpertiseDTO dto) {
        logger.info("POST /video-expertise {}", dto);

        return ResponseEntity.ok(expertiseService.create(dto.federation(), dto.gameNumber(), dto.reportee()));
    }

    @PutMapping(value = "/video-expertise/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<VideoExpertiseDTO> updateExpertise(@PathVariable String id, @RequestBody VideoExpertiseDTO dto) {
        logger.info("PUT /video-expertise/{} {}", id, dto);
        return ResponseEntity.ok(expertiseService.update(id, dto));
    }

}
