package ch.stefanjucker.refereecoach.dto;

import javax.validation.constraints.NotNull;

public record CommentReplyDTO(@NotNull Long commentId,
                              @NotNull String comment) {
}
