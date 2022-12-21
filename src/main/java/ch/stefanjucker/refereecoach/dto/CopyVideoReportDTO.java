package ch.stefanjucker.refereecoach.dto;

import jakarta.validation.constraints.NotNull;

public record CopyVideoReportDTO(@NotNull String sourceId,
                                 @NotNull Reportee reportee) {
}
