package ch.stefanjucker.refereecoach.dto;

import javax.validation.constraints.NotNull;
import java.util.List;

public record VideoReportDiscussionDTO(@NotNull String videoReportId,
                                       @NotNull BasketplanGameDTO basketplanGame,
                                       @NotNull ReporterDTO reporter,
                                       @NotNull String referee,
                                       @NotNull List<VideoCommentDTO> videoComments) {
}
