package ch.stefanjucker.videoexpertise.domain.repository;

import ch.stefanjucker.videoexpertise.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
