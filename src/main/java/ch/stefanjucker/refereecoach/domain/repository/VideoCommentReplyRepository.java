package ch.stefanjucker.refereecoach.domain.repository;

import ch.stefanjucker.refereecoach.domain.VideoCommentReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoCommentReplyRepository extends JpaRepository<VideoCommentReply, Long> {

    List<VideoCommentReply> findByVideoCommentIdOrderByRepliedAt(Long videoCommentId);

}
