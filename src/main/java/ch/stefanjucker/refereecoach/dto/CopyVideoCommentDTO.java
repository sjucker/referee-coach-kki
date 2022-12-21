package ch.stefanjucker.refereecoach.dto;

import jakarta.validation.constraints.NotNull;

public record CopyVideoCommentDTO(@NotNull Long sourceId,
                                  @NotNull Reportee reportee) {
}
