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

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    /**
     * Defines the primary password encoding mechanism for the application using standard BCrypt.
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
                        .requestMatchers("/static/**", "/css/**", "/js/**", "/manifest.json", "/favicon.ico").permitAll()
                        .requestMatchers("/", "/index", "/api/auth/login", "/api/auth/register", "/api/auth/magic-link").permitAll()
                        .requestMatchers("/legal").permitAll()// Index View
                        .requestMatchers("/dashboard", "/dashboard/**").authenticated() // Dashboard
                        .requestMatchers("/profile/", "/profile/**").authenticated()// Profile settings
                        .requestMatchers("/content/", "/content/**").authenticated() // Content manager
                        .requestMatchers("/events/", "/events/**").authenticated()// event manager
                        .requestMatchers("/admin/", "/admin/**").hasRole("ADMIN") // todo consider necessity
                        .requestMatchers("/account").authenticated() // account management
                        .requestMatchers("/community").authenticated() // not yet existent in v1
                        .requestMatchers("/api/validate/**").permitAll()
                        .requestMatchers("/api/public/**").permitAll() // Public APIs
                        .requestMatchers("/api/admin/**").hasRole("ADMIN") // Admin APIs

                        // All other requests require authentication
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/index")
                        .loginProcessingUrl("/api/auth/login")
                        .defaultSuccessUrl("/dashboard")
                        .failureUrl("/index?error=true")
                        .permitAll()) // Keep default form login for now
                .logout(logout -> logout
                        .logoutSuccessUrl("/index")
                        .logoutSuccessUrl("/index?logout=true")
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