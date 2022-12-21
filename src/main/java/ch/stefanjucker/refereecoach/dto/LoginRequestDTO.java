package ch.stefanjucker.refereecoach.dto;

import jakarta.validation.constraints.NotNull;

public record LoginRequestDTO(@NotNull String email, @NotNull String password) {
}
