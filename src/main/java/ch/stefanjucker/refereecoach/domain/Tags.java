package ch.stefanjucker.refereecoach.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Tags {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
}
