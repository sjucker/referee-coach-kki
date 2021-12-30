package ch.stefanjucker.refereecoach.domain;

import ch.stefanjucker.refereecoach.dto.OfficiatingMode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.time.LocalDate;

import static javax.persistence.EnumType.STRING;

@Embeddable
@Getter
@Setter
@ToString(of = "gameNumber")
public class BasketplanGame {

    @Column(nullable = false, name = "game_number")
    private String gameNumber;

    @Column(nullable = false)
    private String competition;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String result;

    @Column(nullable = false, name = "team_a")
    private String teamA;

    @Column(nullable = false, name = "team_b")
    private String teamB;

    @Enumerated(STRING)
    @Column(nullable = false, name = "officiating_mode")
    private OfficiatingMode officiatingMode;

    @OneToOne
    @JoinColumn(name = "referee1_id")
    private Referee referee1;

    @OneToOne
    @JoinColumn(name = "referee2_id")
    private Referee referee2;

    @OneToOne
    @JoinColumn(name = "referee3_id")
    private Referee referee3;

    @Column(name = "youtube_id")
    private String youtubeId;
}
