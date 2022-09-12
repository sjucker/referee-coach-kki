package ch.stefanjucker.refereecoach.dto;

import javax.validation.constraints.NotNull;

public record CopyVideoCommentDTO(@NotNull Long sourceId,
                                  @NotNull Reportee reportee) {
}
