package ch.stefanjucker.refereecoach.rest;

import ch.stefanjucker.refereecoach.domain.repository.UserRepository;
import ch.stefanjucker.refereecoach.dto.ChangePasswordRequestDTO;
import ch.stefanjucker.refereecoach.dto.LoginRequestDTO;
import ch.stefanjucker.refereecoach.dto.LoginResponseDTO;
import ch.stefanjucker.refereecoach.security.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
@RestController
@RequestMapping("/api/authenticate")
public class AuthenticationResource {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationResource(JwtService jwtService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        log.info("POST /api/authenticate {}", request.email());

        var user = userRepository.findByEmail(request.email()).orElse(null);

        if (user != null) {
            if (passwordEncoder.matches(request.password(), user.getPassword())) {

                user.setLastLogin(LocalDateTime.now());
                userRepository.save(user);

                return ResponseEntity.ok(new LoginResponseDTO(
                        user.getId(),
                        user.getName(),
                        user.isAdmin(),
                        jwtService.createJwt(request.email())
                ));
            } else {
                log.warn("password did not match for: {}", request.email());
            }
        } else {
            log.warn("no user found with email: {}", request.email());
        }

        return ResponseEntity.status(UNAUTHORIZED).build();
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@AuthenticationPrincipal UserDetails principal,
                                            @RequestBody @Valid ChangePasswordRequestDTO request) {
        var user = userRepository.findByEmail(principal.getUsername()).orElseThrow();

        log.info("POST /api/authenticate/change-password {}", user.getEmail());

        if (passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(request.newPassword()));
            userRepository.save(user);
            log.info("successfully changed password for: {}", user.getEmail());
            return ResponseEntity.accepted().build();
        } else {
            log.warn("provided old password did not match password in database");
        }

        return ResponseEntity.badRequest().build();
    }
}
