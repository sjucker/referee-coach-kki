package ch.stefanjucker.refereecoach.domain.repository;

import ch.stefanjucker.refereecoach.domain.Referee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefereeRepository extends JpaRepository<Referee, Long> {

    Optional<Referee> findByName(String name);
}
