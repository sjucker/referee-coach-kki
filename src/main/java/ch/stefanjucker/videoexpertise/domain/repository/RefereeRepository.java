package ch.stefanjucker.videoexpertise.domain.repository;

import ch.stefanjucker.videoexpertise.domain.Referee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefereeRepository extends JpaRepository<Referee, Long> {
}
