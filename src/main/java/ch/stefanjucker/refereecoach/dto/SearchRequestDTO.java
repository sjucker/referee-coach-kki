package ch.stefanjucker.refereecoach.dto;

import jakarta.validation.constraints.NotNull;
import java.util.Set;

public record SearchRequestDTO(@NotNull Set<TagDTO> tags) {
}
