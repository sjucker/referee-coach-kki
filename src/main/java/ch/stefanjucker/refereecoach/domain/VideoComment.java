package ch.stefanjucker.refereecoach.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.PositiveOrZero;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "video_report_comment")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class VideoComment {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @PositiveOrZero
    @Column(nullable = false)
    private Integer timestamp;

    @Column(nullable = false, length = 1024)
    private String comment;

    @Column(name = "video_report_id", nullable = false)
    private String videoReportId;

    @ManyToMany(cascade = MERGE)
    @JoinTable(
            name = "video_report_comment_tags",
            joinColumns = {@JoinColumn(name = "video_report_comment_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")}
    )
    private Set<Tags> tags = new HashSet<>();

}
