package ch.stefanjucker.refereecoach.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.List;

public record VideoReportDTO(@NotNull String id,
                             @NotNull BasketplanGameDTO basketplanGame,
                             @NotNull ReporterDTO reporter,
                             @NotNull Reportee reportee,
                             @Length(max = 1024) String imageComment,
                             @Length(max = 1024) String mechanicsComment,
                             @Length(max = 1024) String foulsComment,
                             @Length(max = 1024) String gameManagementComment,
                             @Length(max = 1024) String pointsToKeepComment,
                             @Length(max = 1024) String pointsToImproveComment,
                             @NotNull List<VideoCommentDTO> videoComments,
                             boolean finished) {
}
