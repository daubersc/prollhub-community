package com.prollhub.community.config;

import com.prollhub.community.persistency.model.Account; // Your Account entity
import com.prollhub.community.persistency.repository.AccountRepository; // Your Account repository


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value; // For injecting properties

import java.util.Collections; // Or Set.of() for roles
import java.util.Optional;
// Import Set if using multiple roles
// import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j // Lombok logger
public class DataInitializer implements CommandLineRunner {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    // Inject RoleRepository if needed
    // private final RoleRepository roleRepository;

    // Inject initial user details from application.properties/yml (Recommended!)
    @Value("${app.initial-admin.username:admin}") // Default to 'admin' if property not set
    private String initialAdminUsername;

    @Value("${app.initial-admin.email:admin@example.com}") // Default email
    private String initialAdminEmail;

    @Value("${app.initial-admin.password}") // Require password to be set in properties/env
    private String initialAdminPassword;


    @Override
    public void run(String... args) throws Exception {
        log.info("Checking for initial admin user...");

        // 1. Check if the admin user already exists
        Optional<Account> existingAdmin = accountRepository.findByUsername(initialAdminUsername);
        // Or find by email: Optional<Account> existingAdmin = accountRepository.findByEmail(initialAdminEmail);

        if (existingAdmin.isEmpty()) {
            log.info("Initial admin user '{}' not found. Creating...", initialAdminUsername);

            // Optional: Find or create roles if needed
            // Role adminRole = roleRepository.findByName("ROLE_ADMIN")
            //        .orElseGet(() -> roleRepository.save(new Role("ROLE_ADMIN")));
            // Role userRole = roleRepository.findByName("ROLE_USER")
            //        .orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));

            // 2. Create the new Account entity
            Account adminAccount = new Account();
            adminAccount.setUsername(initialAdminUsername);
            adminAccount.setEmail(initialAdminEmail);

            // 3. Encode the raw password using the application's PasswordEncoder
            adminAccount.setPassword(passwordEncoder.encode(initialAdminPassword));

            adminAccount.setEnabled(true); // Make sure the account is enabled
            // Set other necessary fields (e.g., creation date) if applicable
            // adminAccount.setCreatedAt(Instant.now());

            // 4. Assign roles (if using roles)
            // adminAccount.setRoles(Set.of(adminRole, userRole)); // Example if using Set<Role>
            // Or maybe just:
            // adminAccount.setRoles(Collections.singleton(adminRole));

            // 5. Save the new account to the database
            accountRepository.save(adminAccount);
            log.info("Initial admin user '{}' created successfully.", initialAdminUsername);

        } else {
            log.info("Initial admin user '{}' already exists. No action taken.", initialAdminUsername);
        }
    }
}