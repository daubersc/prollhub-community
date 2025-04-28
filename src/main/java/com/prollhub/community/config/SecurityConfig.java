package com.prollhub.community.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * Defines the primary password encoding mechanism for the application.
     * Uses BCrypt, which is the standard recommendation.
     *
     * @return A PasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Allow access to the root path and index.html without authentication
                        .requestMatchers(
                                "/",                // Root path
                                "/index.html",      // Explicit index file
                                "/favicon.ico",
                                "/manifest.json",   // Example for PWAs
                                "/error",           // Default error page
                                // Static resources
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/static/**",       // Catch-all for static if needed
                                // Public API endpoints (example)
                                "/api/public/**"
                        ).permitAll()

                        // Example: Secure other paths (adjust as needed later)
                        // .requestMatchers("/view1/**").hasRole("USER")
                        // .requestMatchers("/api/secure/**").authenticated()

                        // All other requests require authentication (example default)
                        .anyRequest().authenticated()
                )
                .formLogin(withDefaults()) // Keep default form login for now
                .logout(logout -> logout
                        .logoutSuccessUrl("/logout")
                        .logoutSuccessUrl("/?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll());

        return http.build();
    }

    /**
     * Exposes the AuthenticationManager as a Bean.
     * Required for certain advanced configurations or manual authentication.
     *
     * @param authenticationConfiguration Standard configuration object.
     * @return The configured AuthenticationManager.
     * @throws Exception If configuration fails.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}