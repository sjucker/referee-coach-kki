package ch.stefanjucker.refereecoach.dto;

import javax.validation.constraints.NotNull;
import java.util.List;

public record VideoReportDTO(@NotNull String id,
                             @NotNull BasketplanGameDTO basketplanGame,
                             @NotNull ReporterDTO reporter,
                             @NotNull Reportee reportee,
                             @NotNull String imageComment,
                             @NotNull String mechanicsComment,
                             @NotNull String foulsComment,
                             @NotNull String gameManagementComment,
                             @NotNull String pointsToKeepComment,
                             @NotNull String pointsToImproveComment,
                             @NotNull List<VideoCommentDTO> videoComments,
                             boolean finished) {
}
