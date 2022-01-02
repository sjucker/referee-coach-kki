package ch.stefanjucker.refereecoach.dto;

import javax.validation.constraints.NotNull;

public record CopyVideoReportDTO(@NotNull String sourceId,
                                 @NotNull Reportee reportee) {
}
