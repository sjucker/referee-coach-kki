package ch.stefanjucker.refereecoach.dto;

import ch.stefanjucker.refereecoach.service.basketplan.BasketplanService.Federation;

public record CreateVideoExpertiseDTO(Federation federation, String gameNumber, Reportee reportee) {
}
