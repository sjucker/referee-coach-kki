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
import javax.persistence.Table;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "video_report_comment_reply")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class VideoCommentReply {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String repliedBy;

    @Column(nullable = false)
    private LocalDateTime repliedAt;

    @Column(nullable = false, length = 1024)
    private String reply;

    @Column(name = "video_report_comment_id", nullable = false)
    private Long videoCommentId;
}
