package com.modsen.booktrackerservice.config;

import com.modsen.booktrackerservice.security.JwtTokenFilter;
import com.modsen.booktrackerservice.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
@EnableGlobalAuthentication
@EnableMethodSecurity
@Slf4j
public class ApplicationConfig {

    private final JwtTokenProvider jwtTokenProvider;


    @Bean
    public PasswordEncoder passwordEncoder() {
        log.info("Creating a new PasswordEncoder (BCryptPasswordEncoder) bean.");
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        log.info("Creating a new AuthenticationManager bean.");
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        log.info("Configuring security filter chain.");
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(STATELESS);
                    log.info("Session management set to stateless.");
                })
                .exceptionHandling(configurer ->
                        configurer.authenticationEntryPoint(
                                        (request, response, authException) -> {
                                            log.error("Authentication failed: Unauthorized access.");
                                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                                            response.getWriter().write("Unauthorized");
                                        })
                                .accessDeniedHandler(
                                        (request, response, accessDeniedException) -> {
                                            log.error("Access denied: Insufficient permissions.");
                                            response.setStatus(HttpStatus.FORBIDDEN.value());
                                            response.getWriter().write("Access denied.");
                                        }
                                ))
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/v1/**").hasRole("USER")
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/swagger-ui.html").permitAll()
                        .anyRequest().authenticated()
                );
        log.info("Security filter chain configured successfully.");
        httpSecurity.addFilterBefore(new JwtTokenFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
        log.info("JWT filter added before UsernamePasswordAuthenticationFilter.");
        return httpSecurity.build();
    }

    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        log.info("Creating a new HiddenHttpMethodFilter bean.");
        return new HiddenHttpMethodFilter();
    }
}