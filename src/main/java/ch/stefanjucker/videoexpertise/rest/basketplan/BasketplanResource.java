package ch.stefanjucker.videoexpertise.rest.basketplan;

import ch.stefanjucker.videoexpertise.dto.basketplan.BasketplanGame;
import ch.stefanjucker.videoexpertise.service.basketplan.BasketplanService;
import ch.stefanjucker.videoexpertise.service.basketplan.BasketplanService.Federation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BasketplanResource {

    private final BasketplanService basketplanService;

    public BasketplanResource(BasketplanService basketplanService) {
        this.basketplanService = basketplanService;
    }

    @GetMapping("/game/{federation}/{gameNumber}")
    public ResponseEntity<BasketplanGame> game(@PathVariable Federation federation, @PathVariable String gameNumber) {
        try {
            return ResponseEntity.of(basketplanService.findGameByNumber(federation, gameNumber));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
