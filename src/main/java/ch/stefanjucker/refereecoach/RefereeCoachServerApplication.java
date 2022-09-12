package ch.stefanjucker.refereecoach;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class RefereeCoachServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RefereeCoachServerApplication.class, args);
    }

}
