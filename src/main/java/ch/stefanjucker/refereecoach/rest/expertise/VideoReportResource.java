package ch.stefanjucker.refereecoach.rest.expertise;

import ch.stefanjucker.refereecoach.dto.CreateVideoReportDTO;
import ch.stefanjucker.refereecoach.dto.VideoReportDTO;
import ch.stefanjucker.refereecoach.service.expertise.VideoReportService;
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
public class VideoReportResource {

    private static final Logger logger = LoggerFactory.getLogger(VideoReportResource.class);

    private final VideoReportService videoReportService;

    public VideoReportResource(VideoReportService videoReportService) {
        this.videoReportService = videoReportService;
    }

    @GetMapping(value = "/video-expertise")
    public ResponseEntity<List<VideoReportDTO>> getAllExpertise() {
        logger.info("GET /video-expertise");
        return ResponseEntity.ok(videoReportService.findAll());
    }

    @GetMapping(value = "/video-expertise/{id}")
    public ResponseEntity<VideoReportDTO> getExpertise(@PathVariable String id) {
        logger.info("GET /video-expertise/{}", id);

        return videoReportService.find(id)
                                 .map(ResponseEntity::ok)
                                 .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/video-expertise", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<VideoReportDTO> createExpertise(@RequestBody CreateVideoReportDTO dto) {
        logger.info("POST /video-expertise {}", dto);

        return ResponseEntity.ok(videoReportService.create(dto.federation(), dto.gameNumber(), dto.reportee()));
    }

    @PutMapping(value = "/video-expertise/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<VideoReportDTO> updateExpertise(@PathVariable String id, @RequestBody VideoReportDTO dto) {
        logger.info("PUT /video-expertise/{} {}", id, dto);
        return ResponseEntity.ok(videoReportService.update(id, dto));
    }

}
