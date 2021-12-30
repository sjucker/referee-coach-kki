package ch.stefanjucker.refereecoach.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public record VideoCommentDTO(@NotNull Integer timestamp,
                              @NotNull @Length(max = 1024) String comment) {
}
