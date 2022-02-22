package ch.stefanjucker.refereecoach.dto;

import javax.validation.constraints.NotNull;
import java.util.List;

public record CreateRepliesDTO(@NotNull List<CommentReplyDTO> replies) {
}
