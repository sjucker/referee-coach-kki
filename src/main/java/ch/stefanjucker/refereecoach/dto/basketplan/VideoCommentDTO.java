package ch.stefanjucker.refereecoach.dto.basketplan;

import javax.validation.constraints.NotNull;

public record VideoCommentDTO(@NotNull Integer timestamp,
                              @NotNull String comment) {
}
