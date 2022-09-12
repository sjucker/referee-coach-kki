package ch.stefanjucker.refereecoach.dto;

import javax.validation.constraints.NotNull;

public record TagDTO(@NotNull Long id,
                     @NotNull String name) {
}
