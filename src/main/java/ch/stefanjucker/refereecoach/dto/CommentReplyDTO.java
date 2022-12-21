package ch.stefanjucker.refereecoach.dto;

import jakarta.validation.constraints.NotNull;

public record CommentReplyDTO(@NotNull Long commentId,
                              @NotNull String comment) {
}
