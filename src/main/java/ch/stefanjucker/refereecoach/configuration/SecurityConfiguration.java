package ch.stefanjucker.refereecoach.configuration;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import ch.stefanjucker.refereecoach.security.JwtRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    private final JwtRequestFilter jwtRequestFilter;

    public SecurityConfiguration(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        var config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addExposedHeader(AUTHORIZATION);
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public DefaultSecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .httpBasic().disable()
            .cors().and().csrf().disable()
            .authorizeHttpRequests()
                .requestMatchers(OPTIONS, "/api/**").permitAll()
                .requestMatchers(POST, "/api/authenticate").permitAll()
                // read-only report also available to anonymous users (i.e., the referees)
                .requestMatchers(GET, "/api/video-report/*").permitAll()
                .requestMatchers("/api/video-report/*/discussion").permitAll()
                .requestMatchers("/api/**").authenticated()
                .anyRequest().permitAll()
            .and().exceptionHandling()
            .and().sessionManagement().sessionCreationPolicy(STATELESS);

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        // @formatter:on

        return http.build();
    }

}
