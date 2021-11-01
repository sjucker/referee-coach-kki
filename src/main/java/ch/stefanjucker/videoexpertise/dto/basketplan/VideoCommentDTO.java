package ch.stefanjucker.videoexpertise.dto.basketplan;

import javax.validation.constraints.NotNull;

public record VideoCommentDTO(@NotNull Integer timestamp,
                              @NotNull String comment) {
}
