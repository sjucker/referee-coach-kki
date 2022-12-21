package ch.stefanjucker.refereecoach.dto;

import jakarta.validation.constraints.NotNull;

public record TagDTO(@NotNull Long id,
                     @NotNull String name) {
}
