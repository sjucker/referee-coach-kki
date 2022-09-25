package ch.stefanjucker.refereecoach.dto;

import javax.validation.constraints.NotNull;
import java.util.Set;

public record SearchRequestDTO(@NotNull Set<TagDTO> tags) {
}
