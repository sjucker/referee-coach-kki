package ch.stefanjucker.refereecoach.dto;

import org.hibernate.validator.constraints.Length;

public record CriteriaEvaluationDTO(@Length(max = 1024) String comment,
                                    Double score,
                                    String rating) {
}
