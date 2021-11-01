package ch.stefanjucker.videoexpertise.rest.expertise;

import ch.stefanjucker.videoexpertise.dto.basketplan.ExpertiseDTO;
import ch.stefanjucker.videoexpertise.service.expertise.ExpertiseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class ExpertiseResource {

    private static final Logger logger = LoggerFactory.getLogger(ExpertiseResource.class);

    private final ExpertiseService expertiseService;

    public ExpertiseResource(ExpertiseService expertiseService) {
        this.expertiseService = expertiseService;
    }

    @GetMapping(value = "/expertise/{id}")
    public ResponseEntity<ExpertiseDTO> getExpertise(@PathVariable String id) {
        return expertiseService.find(id)
                               .map(ResponseEntity::ok)
                               .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/expertise", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ExpertiseDTO> postExpertise(@RequestBody ExpertiseDTO dto) {
        logger.info("POST /expertise {}", dto);

        return ResponseEntity.ok(expertiseService.save(dto));
    }

    @PatchMapping(value = "/expertise/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ExpertiseDTO> patchExpertise(@PathVariable String id, @RequestBody ExpertiseDTO dto) {
        logger.info("PATCH /expertise/{} {}", id, dto);

        return ResponseEntity.ok(expertiseService.update(id, dto));
    }

}
