package ch.stefanjucker.videoexpertise.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.EnumType.STRING;

@Entity
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

    public Referee() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RefereeLevel getLevel() {
        return level;
    }

    public void setLevel(RefereeLevel level) {
        this.level = level;
    }

    public enum RefereeLevel {
        GROUP_1,
        GROUP_2,
        GROUP_3
    }
}
