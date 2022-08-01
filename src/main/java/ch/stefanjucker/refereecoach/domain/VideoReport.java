package ch.stefanjucker.refereecoach.domain;

import ch.stefanjucker.refereecoach.dto.Reportee;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Objects;
import java.util.stream.Stream;

import static javax.persistence.EnumType.STRING;

@Entity
@Table(name = "video_report")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class VideoReport {

    public static final int CURRENT_VERSION = 2;

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

    @Column(length = 1024, name = "general_comment")
    private String generalComment;

    @AttributeOverrides({
            @AttributeOverride(name = "comment", column = @Column(name = "image_comment")),
            @AttributeOverride(name = "score", column = @Column(name = "image_score"))
    })
    @Embedded
    private CriteriaEvaluation image = new CriteriaEvaluation("", 7.0);

    @AttributeOverrides({
            @AttributeOverride(name = "comment", column = @Column(name = "fitness_comment")),
            @AttributeOverride(name = "score", column = @Column(name = "fitness_score"))
    })
    @Embedded
    private CriteriaEvaluation fitness = new CriteriaEvaluation("", 7.0);

    @AttributeOverrides({
            @AttributeOverride(name = "comment", column = @Column(name = "mechanics_comment")),
            @AttributeOverride(name = "score", column = @Column(name = "mechanics_score"))
    })
    @Embedded
    private CriteriaEvaluation mechanics = new CriteriaEvaluation("", 7.0);

    @AttributeOverrides({
            @AttributeOverride(name = "comment", column = @Column(name = "fouls_comment")),
            @AttributeOverride(name = "score", column = @Column(name = "fouls_score"))
    })
    @Embedded
    private CriteriaEvaluation fouls = new CriteriaEvaluation("", 7.0);

    @AttributeOverrides({
            @AttributeOverride(name = "comment", column = @Column(name = "violations_comment")),
            @AttributeOverride(name = "score", column = @Column(name = "violations_score"))
    })
    @Embedded
    private CriteriaEvaluation violations = new CriteriaEvaluation("", 7.0);

    @AttributeOverrides({
            @AttributeOverride(name = "comment", column = @Column(name = "game_management_comment")),
            @AttributeOverride(name = "score", column = @Column(name = "game_management_score"))
    })
    @Embedded
    private CriteriaEvaluation gameManagement = new CriteriaEvaluation("", 7.0);

    @Column(length = 1024, name = "points_to_keep_comment")
    private String pointsToKeepComment;
    @Column(length = 1024, name = "points_to_improve_comment")
    private String pointsToImproveComment;

    @Column(nullable = false)
    private boolean finished;

    private int version;

    public Referee relevantReferee() {
        return switch (reportee) {
            case FIRST_REFEREE -> basketplanGame.getReferee1();
            case SECOND_REFEREE -> basketplanGame.getReferee2();
            case THIRD_REFEREE -> basketplanGame.getReferee3();
        };
    }

    public double getOverallScore() {
        return Stream.of(image.getScore(), fitness.getScore(), mechanics.getScore(), fouls.getScore(), violations.getScore(), gameManagement.getScore())
                     .filter(Objects::nonNull)
                     .mapToDouble(d -> d)
                     .average()
                     .orElse(0.0);
    }

    public String getOverallRating() {
        return CriteriaEvaluation.getRating(getOverallScore());
    }

}
