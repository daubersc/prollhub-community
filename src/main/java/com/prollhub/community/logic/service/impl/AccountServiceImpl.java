package com.prollhub.community.logic.service.impl;

import com.prollhub.community.dto.auth.RegisterRequest;
import com.prollhub.community.exception.DuplicateUsernameException;
import com.prollhub.community.logic.service.AccountService;
import com.prollhub.community.persistency.model.Account;
import com.prollhub.community.persistency.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Implementation of the AccountService interface.
 */
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private static final Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder; // Inject the password encoder

    @Override
    @Transactional // Ensure the operation is atomic
    public Account registerNewAccount(RegisterRequest registerRequest) {
        log.info("Attempting to register new account for username: {}", registerRequest.getUsername());

        // --- Validation ---
        // Check if username already exists
        if (accountRepository.existsByUsername(registerRequest.getUsername())) {
            log.warn("Registration failed: Username '{}' already exists.", registerRequest.getUsername());
            // Consider throwing a custom exception (e.g., UserAlreadyExistsException)
            throw new DuplicateUsernameException("Username is already taken");
        }

        // Check if email already exists
        if (accountRepository.existsByEmail(registerRequest.getEmail())) {
            log.warn("Registration failed: Email '{}' already exists.", registerRequest.getEmail());
            throw new DuplicateUsernameException("Email Address already in use!");
        }

        // --- Create new account ---
        Account account = new Account();
        account.setUsername(registerRequest.getUsername());
        account.setEmail(registerRequest.getEmail());

        // Hash the password before saving
        account.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        // Assign default roles (e.g., "USER")
        // Roles should be stored without the "ROLE_" prefix in the Set if the
        // getAuthorities() method in Account adds it.
        Set<String> defaultRoles = new HashSet<>();
        defaultRoles.add("USER"); // Example: Add "USER" role by default
        account.setRoles(defaultRoles);

        // Set default status flags (enabled=true, etc.) - these are defaults in Account entity
        // account.setEnabled(true); // Already defaulted in entity

        // --- Save account ---
        Account savedAccount = accountRepository.save(account);
        log.info("Successfully registered new account for username: {} with ID: {}", savedAccount.getUsername(), savedAccount.getId());

        return savedAccount;
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Account> findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Account> findById(UUID id) {
        return accountRepository.findById(id);
    }

    // Implement other methods (delete, update) here
}
