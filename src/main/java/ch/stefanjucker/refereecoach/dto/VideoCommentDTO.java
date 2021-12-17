package ch.stefanjucker.refereecoach.dto;

import javax.validation.constraints.NotNull;

public record VideoCommentDTO(@NotNull Integer timestamp,
                              @NotNull String comment) {
}
