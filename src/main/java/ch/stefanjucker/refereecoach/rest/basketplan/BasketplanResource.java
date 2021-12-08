package ch.stefanjucker.refereecoach.rest.basketplan;

import ch.stefanjucker.refereecoach.dto.basketplan.BasketplanGameDTO;
import ch.stefanjucker.refereecoach.service.basketplan.BasketplanService;
import ch.stefanjucker.refereecoach.service.basketplan.BasketplanService.Federation;
 import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BasketplanResource {

    private static final Logger logger = LoggerFactory.getLogger(BasketplanResource.class);

    private final BasketplanService basketplanService;

    public BasketplanResource(BasketplanService basketplanService) {
        this.basketplanService = basketplanService;
    }

    @GetMapping("/game/{federation}/{gameNumber}")
    public ResponseEntity<BasketplanGameDTO> game(@PathVariable Federation federation, @PathVariable String gameNumber) {
        logger.info("GET /game/{}/{}", federation.getId(), gameNumber);

        try {
            return ResponseEntity.of(basketplanService.findGameByNumber(federation, gameNumber));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
