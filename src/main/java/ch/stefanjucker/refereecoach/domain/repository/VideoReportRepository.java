package ch.stefanjucker.refereecoach.domain.repository;

import ch.stefanjucker.refereecoach.domain.VideoReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoReportRepository extends JpaRepository<VideoReport, String> {

    List<VideoReport> findByBasketplanGameGameNumberAndReporterId(String gameNumber, Long id);
}
