package ch.stefanjucker.refereecoach.domain.repository;

import ch.stefanjucker.refereecoach.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
