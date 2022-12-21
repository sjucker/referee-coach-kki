package ch.stefanjucker.refereecoach.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public record SearchResponseDTO(@NotNull List<VideoCommentDetailDTO> results) {
}
