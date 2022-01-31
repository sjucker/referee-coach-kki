package ch.stefanjucker.refereecoach.dto;

import javax.validation.constraints.NotNull;

public record CreateCommentReplyDTO(@NotNull String reply) {
}
