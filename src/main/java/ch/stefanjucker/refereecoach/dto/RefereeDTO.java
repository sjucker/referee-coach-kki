package ch.stefanjucker.refereecoach.dto;

import jakarta.validation.constraints.NotNull;

public record RefereeDTO(@NotNull Long id, @NotNull String name) {
}
