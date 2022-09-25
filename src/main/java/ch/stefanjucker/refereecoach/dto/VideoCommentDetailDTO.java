package ch.stefanjucker.refereecoach.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.Set;

public record VideoCommentDetailDTO(@NotNull BasketplanGameDTO basketplanGame,
                                    @NotNull Integer timestamp,
                                    @NotNull @Length(max = 1024) String comment,
                                    @NotNull Set<TagDTO> tags) {
}
