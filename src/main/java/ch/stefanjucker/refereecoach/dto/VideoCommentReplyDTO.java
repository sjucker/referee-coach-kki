package ch.stefanjucker.refereecoach.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record VideoCommentReplyDTO(@NotNull Long id,
                                   @NotNull String repliedBy,
                                   @NotNull LocalDateTime repliedAt,
                                   @NotNull @Length(max = 1024) String reply) {
}
