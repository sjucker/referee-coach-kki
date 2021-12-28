package ch.stefanjucker.refereecoach.dto;

import javax.validation.constraints.NotNull;

public record RefereeDTO(@NotNull Long id, @NotNull String name) {
}
