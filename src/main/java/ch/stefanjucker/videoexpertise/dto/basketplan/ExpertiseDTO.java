package ch.stefanjucker.videoexpertise.dto.basketplan;

import javax.validation.constraints.NotNull;
import java.util.List;

public record ExpertiseDTO(String id,
                           @NotNull BasketplanGameDTO basketplanGame,
                           @NotNull String imageComment,
                           @NotNull String mechanicsComment,
                           @NotNull String foulsComment,
                           @NotNull String gameManagementComment,
                           @NotNull String pointsToKeepComment,
                           @NotNull String pointsToImproveComment,
                           @NotNull List<VideoCommentDTO> videoComments) {
}
