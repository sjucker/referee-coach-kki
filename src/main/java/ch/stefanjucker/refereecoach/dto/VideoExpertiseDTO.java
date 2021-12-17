package ch.stefanjucker.refereecoach.dto;

import javax.validation.constraints.NotNull;
import java.util.List;

public record VideoExpertiseDTO(@NotNull String id,
                                @NotNull BasketplanGameDTO basketplanGame,
                                @NotNull Reportee reportee,
                                @NotNull String imageComment,
                                @NotNull String mechanicsComment,
                                @NotNull String foulsComment,
                                @NotNull String gameManagementComment,
                                @NotNull String pointsToKeepComment,
                                @NotNull String pointsToImproveComment,
                                @NotNull List<VideoCommentDTO> videoComments,
                                boolean finished) {

    public String relevantReferee() {
        return switch (reportee) {
            case FIRST_REFEREE -> basketplanGame.referee1();
            case SECOND_REFEREE -> basketplanGame.referee2();
            case THIRD_REFEREE -> basketplanGame.referee3();
        };
    }

}
