package ch.stefanjucker.videoexpertise.dto.basketplan;

import java.time.LocalDate;

public record BasketplanGame(String competition,
                             LocalDate date,
                             String result,
                             String teamA,
                             String teamB,
                             String referee1,
                             String referee2,
                             String referee3,
                             String youtubeId) {
}
