package ch.stefanjucker.refereecoach.rest;

import ch.stefanjucker.refereecoach.dto.CreateVideoReportDTO;
import ch.stefanjucker.refereecoach.dto.VideoReportDTO;
import ch.stefanjucker.refereecoach.service.VideoReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping("/api/video-report")
public class VideoReportResource {

    private final VideoReportService videoReportService;

    public VideoReportResource(VideoReportService videoReportService) {
        this.videoReportService = videoReportService;
    }

    @GetMapping
    public ResponseEntity<List<VideoReportDTO>> getAllReports() {
        log.info("GET /video-report");
        return ResponseEntity.ok(videoReportService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideoReportDTO> getVideoReport(@PathVariable String id) {
        log.info("GET /video-report/{}", id);

        return videoReportService.find(id)
                                 .map(ResponseEntity::ok)
                                 .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<VideoReportDTO> createVideoReport(@RequestBody CreateVideoReportDTO dto) {
        log.info("POST /video-report {}", dto);

        return ResponseEntity.ok(videoReportService.create(dto.federation(), dto.gameNumber(), dto.reportee()));
    }

    @PutMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<VideoReportDTO> updateVideoReport(@PathVariable String id, @RequestBody VideoReportDTO dto) {
        log.info("PUT /video-report/{} {}", id, dto);
        return ResponseEntity.ok(videoReportService.update(id, dto));
    }

}
