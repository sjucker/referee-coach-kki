package ch.stefanjucker.videoexpertise.dto.basketplan;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public record BasketplanGameDTO(@NotNull String competition,
                                @NotNull LocalDate date,
                                @NotNull String result,
                                @NotNull String teamA,
                                @NotNull String teamB,
                                @NotNull String referee1,
                                @NotNull String referee2,
                                String referee3,
                                String youtubeId) {
}
