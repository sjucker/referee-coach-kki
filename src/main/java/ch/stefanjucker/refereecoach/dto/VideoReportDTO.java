package ch.stefanjucker.refereecoach.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.List;

public record VideoReportDTO(@NotNull String id,
                             @NotNull BasketplanGameDTO basketplanGame,
                             @NotNull ReporterDTO reporter,
                             @NotNull Reportee reportee,
                             double overallScore,
                             @NotNull String overallRating,
                             @Length(max = 1024) String generalComment,
                             @NotNull CriteriaEvaluationDTO image,
                             @NotNull CriteriaEvaluationDTO fitness,
                             @NotNull CriteriaEvaluationDTO mechanics,
                             @NotNull CriteriaEvaluationDTO fouls,
                             @NotNull CriteriaEvaluationDTO violations,
                             @NotNull CriteriaEvaluationDTO gameManagement,
                             @Length(max = 1024) String pointsToKeepComment,
                             @Length(max = 1024) String pointsToImproveComment,
                             @NotNull List<VideoCommentDTO> videoComments,
                             boolean finished,
                             int version) {
}
