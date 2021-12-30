package ch.stefanjucker.refereecoach.dto;

import ch.stefanjucker.refereecoach.service.BasketplanService.Federation;

public record CreateVideoReportDTO(Federation federation, String gameNumber, Reportee reportee) {
}
