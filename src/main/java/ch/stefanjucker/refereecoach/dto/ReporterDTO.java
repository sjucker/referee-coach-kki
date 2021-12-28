package ch.stefanjucker.refereecoach.dto;

import javax.validation.constraints.NotNull;

public record ReporterDTO(@NotNull Long id, @NotNull String name) {
}
