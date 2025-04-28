package com.prollhub.community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        // Allow access to the root path and index.html without authentication
                        .requestMatchers("/", "/index.html").permitAll()
                        // Also permit access to standard static resource locations if needed
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()

                        // Example: Secure other paths (adjust as needed later)
                        // .requestMatchers("/view1/**").hasRole("USER")
                        // .requestMatchers("/api/secure/**").authenticated()

                        // All other requests require authentication (example default)
                        .anyRequest().authenticated()
                )
                .formLogin(withDefaults()) // Keep default form login for now
                .logout(logout -> logout.logoutSuccessUrl("/").permitAll());

        return http.build();
    }

    // Remember to also configure a UserDetailsService and PasswordEncoder bean later
    // For now, Spring Boot might provide a default user/password if no UserDetailsService is defined
}