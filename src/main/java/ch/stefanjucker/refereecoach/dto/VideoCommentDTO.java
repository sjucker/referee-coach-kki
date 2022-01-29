package ch.stefanjucker.refereecoach.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.List;

public record VideoCommentDTO(Long id, // nullable for entries that are not yet persisted
                              @NotNull Integer timestamp,
                              @NotNull @Length(max = 1024) String comment,
                              @NotNull List<VideoCommentReplyDTO> replies) {
}
