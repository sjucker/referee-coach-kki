package ch.stefanjucker.refereecoach.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.EnumType.STRING;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Referee {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Enumerated(STRING)
    private RefereeLevel level;

    public enum RefereeLevel {
        GROUP_1,
        GROUP_2,
        GROUP_3
    }
}
