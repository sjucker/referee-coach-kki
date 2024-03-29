package ch.stefanjucker.refereecoach.rest;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM;

import ch.stefanjucker.refereecoach.domain.Coach;
import ch.stefanjucker.refereecoach.domain.repository.CoachRepository;
import ch.stefanjucker.refereecoach.dto.CopyVideoCommentDTO;
import ch.stefanjucker.refereecoach.dto.CopyVideoReportDTO;
import ch.stefanjucker.refereecoach.dto.CreateRepliesDTO;
import ch.stefanjucker.refereecoach.dto.CreateVideoReportDTO;
import ch.stefanjucker.refereecoach.dto.SearchRequestDTO;
import ch.stefanjucker.refereecoach.dto.SearchResponseDTO;
import ch.stefanjucker.refereecoach.dto.TagDTO;
import ch.stefanjucker.refereecoach.dto.VideoCommentReplyDTO;
import ch.stefanjucker.refereecoach.dto.VideoReportDTO;
import ch.stefanjucker.refereecoach.dto.VideoReportDiscussionDTO;
import ch.stefanjucker.refereecoach.service.ExportService;
import ch.stefanjucker.refereecoach.service.SearchService;
import ch.stefanjucker.refereecoach.service.VideoReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/video-report")
public class VideoReportResource {

    private final VideoReportService videoReportService;
    private final CoachRepository coachRepository;
    private final ExportService exportService;
    private final SearchService searchService;

    public VideoReportResource(VideoReportService videoReportService,
                               CoachRepository coachRepository,
                               ExportService exportService,
                               SearchService searchService) {
        this.videoReportService = videoReportService;
        this.coachRepository = coachRepository;
        this.exportService = exportService;
        this.searchService = searchService;
    }

    @GetMapping
    public ResponseEntity<List<VideoReportDTO>> getAllReports(@RequestParam @DateTimeFormat(iso = DATE) LocalDate from,
                                                              @RequestParam @DateTimeFormat(iso = DATE) LocalDate to) {
        log.info("GET /video-report?from={}&to={}", from, to);
        return ResponseEntity.ok(videoReportService.findAll(from, to));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideoReportDTO> getVideoReport(@PathVariable String id) {
        log.info("GET /video-report/{}", id);

        return videoReportService.find(id)
                                 .map(ResponseEntity::ok)
                                 .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<VideoReportDTO> createVideoReport(@AuthenticationPrincipal UserDetails principal,
                                                            @RequestBody @Valid CreateVideoReportDTO dto) {
        var user = coachRepository.findByEmail(principal.getUsername()).orElseThrow();
        log.info("POST /video-report {} ({})", dto, user);

        return ResponseEntity.ok(videoReportService.create(dto.federation(), dto.gameNumber(), dto.youtubeId(), dto.reportee(), user));
    }

    @PostMapping(path = "/copy", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<VideoReportDTO> copyVideoReport(@AuthenticationPrincipal UserDetails principal,
                                                          @RequestBody @Valid CopyVideoReportDTO dto) {
        var user = coachRepository.findByEmail(principal.getUsername()).orElseThrow();
        log.info("POST /video-report/copy {} ({})", dto, user);

        return ResponseEntity.ok(videoReportService.copy(dto.sourceId(), dto.reportee(), user));
    }

    @PutMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<VideoReportDTO> updateVideoReport(@AuthenticationPrincipal UserDetails principal,
                                                            @PathVariable String id,
                                                            @RequestBody @Valid VideoReportDTO dto) {
        var user = coachRepository.findByEmail(principal.getUsername()).orElseThrow();
        log.info("PUT /video-report/{} {} ({})", id, dto, user);
        return ResponseEntity.ok(videoReportService.update(id, dto, user));
    }

    @PostMapping(path = "/copy-comment", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> copyComment(@AuthenticationPrincipal UserDetails principal,
                                         @RequestBody @Valid CopyVideoCommentDTO dto) {
        var user = coachRepository.findByEmail(principal.getUsername()).orElseThrow();
        log.info("POST /video-report/copy-comment {} ({})", dto, user);

        videoReportService.copyVideoComment(dto.sourceId(), dto.reportee(), user);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/{id}/discussion")
    public ResponseEntity<VideoReportDiscussionDTO> getDiscussion(@PathVariable String id) {
        log.info("GET /video-report/{}/discussion", id);

        return ResponseEntity.ok(videoReportService.getVideoReportDiscussion(id));
    }

    @PostMapping(path = "/{id}/discussion")
    public ResponseEntity<VideoCommentReplyDTO> postDiscussion(@AuthenticationPrincipal UserDetails principal,
                                                               @PathVariable String id,
                                                               @RequestBody @Valid CreateRepliesDTO dto) {

        Coach coach = null;
        if (principal != null) {
            coach = coachRepository.findByEmail(principal.getUsername()).orElseThrow();
        }
        log.info("POST /video-report/{}/discussion {} {}", id, dto, coach);

        videoReportService.addReplies(coach, id, dto);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteVideoReport(@AuthenticationPrincipal UserDetails principal,
                                               @PathVariable String id) {
        var user = coachRepository.findByEmail(principal.getUsername()).orElseThrow();
        log.info("DELETE /video-report/{} ({})", id, user);

        videoReportService.delete(id, user);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/export")
    public ResponseEntity<Resource> export() {
        log.info("GET /video-report/export");
        try {
            var file = exportService.export();
            var path = Paths.get(file.getAbsolutePath());
            var resource = new ByteArrayResource(Files.readAllBytes(path));

            return ResponseEntity.ok()
                                 .header(CONTENT_TYPE, Files.probeContentType(path))
                                 .contentLength(file.length())
                                 .contentType(APPLICATION_OCTET_STREAM)
                                 .body(resource);
        } catch (RuntimeException | IOException e) {
            log.error("Export failed", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(path = "/tags")
    public ResponseEntity<List<TagDTO>> tags() {
        log.info("GET /video-report/tags");
        return ResponseEntity.ok(videoReportService.getAllAvailableTags());
    }

    @PostMapping(path = "/search")
    public ResponseEntity<SearchResponseDTO> search(@RequestBody @Valid SearchRequestDTO dto) {
        log.info("POST /video-report/search {}", dto);
        return ResponseEntity.ok(new SearchResponseDTO(searchService.search(dto)));
    }

}
