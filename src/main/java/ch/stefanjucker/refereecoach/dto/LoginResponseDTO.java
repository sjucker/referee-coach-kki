package ch.stefanjucker.refereecoach.dto;

import javax.validation.constraints.NotNull;

public record LoginResponseDTO(@NotNull Long id, @NotNull String name, boolean admin, @NotNull String jwt) {
}
