package ch.stefanjucker.refereecoach.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CriteriaEvaluation {

    @Column(length = 1024)
    private String comment;
    private Double score;

    public String getRating() {
        return getRating(score);
    }

    public static String getRating(Double score) {
        if (score == null) {
            return "-";
        }
        if (score >= 8.6) {
            return "excellent";
        } else if (score >= 8.1) {
            return "very good";
        } else if (score >= 7.6) {
            return "good";
        } else if (score >= 7.1) {
            return "discreet";
        } else if (score >= 6.6) {
            return "sufficient";
        } else {
            return "insufficient";
        }
    }

}
