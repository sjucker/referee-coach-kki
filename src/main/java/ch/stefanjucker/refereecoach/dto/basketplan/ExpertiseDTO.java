package ch.stefanjucker.refereecoach.dto.basketplan;

import javax.validation.constraints.NotNull;
import java.util.List;

public record ExpertiseDTO(String id,
                           @NotNull BasketplanGameDTO basketplanGame,
                           @NotNull Reportee reportee,
                           @NotNull String imageComment,
                           @NotNull String mechanicsComment,
                           @NotNull String foulsComment,
                           @NotNull String gameManagementComment,
                           @NotNull String pointsToKeepComment,
                           @NotNull String pointsToImproveComment,
                           @NotNull List<VideoCommentDTO> videoComments) {
}
