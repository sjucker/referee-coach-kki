package ch.stefanjucker.refereecoach.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.PositiveOrZero;

@Embeddable
@Getter
@Setter
@ToString
@NoArgsConstructor
public class VideoComment {

    @PositiveOrZero
    @Column(nullable = false)
    private Integer timestamp;

    @Column(nullable = false, length = 1024)
    private String comment;

}
