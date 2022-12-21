package ch.stefanjucker.refereecoach.dto;

import jakarta.validation.constraints.NotNull;

public record ChangePasswordRequestDTO(@NotNull String oldPassword, @NotNull String newPassword) {
}
