package ch.stefanjucker.refereecoach.domain;

import ch.stefanjucker.refereecoach.dto.Reportee;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import static javax.persistence.EnumType.STRING;

@Entity
@Table(name = "video_report")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class VideoReport {

    @Id
    @Column(nullable = false)
    private String id;

    @OneToOne
    @JoinColumn(name = "reporter_id")
    private User reporter;

    @Enumerated(STRING)
    @Column(nullable = false)
    private Reportee reportee;

    @Embedded
    private BasketplanGame basketplanGame;

    @Column(length = 1024, name = "image_comment")
    private String imageComment;
    @Column(length = 1024, name = "mechanics_comment")
    private String mechanicsComment;
    @Column(length = 1024, name = "fouls_comment")
    private String foulsComment;
    @Column(length = 1024, name = "game_management_comment")
    private String gameManagementComment;
    @Column(length = 1024, name = "points_to_keep_comment")
    private String pointsToKeepComment;
    @Column(length = 1024, name = "points_to_improve_comment")
    private String pointsToImproveComment;

    @Column(nullable = false)
    private boolean finished;

    public Referee relevantReferee() {
        return switch (reportee) {
            case FIRST_REFEREE -> basketplanGame.getReferee1();
            case SECOND_REFEREE -> basketplanGame.getReferee2();
            case THIRD_REFEREE -> basketplanGame.getReferee3();
        };
    }

}
