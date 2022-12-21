package ch.stefanjucker.refereecoach.dto;

import jakarta.validation.constraints.NotNull;

public record ReporterDTO(@NotNull Long id, @NotNull String name) {
}
