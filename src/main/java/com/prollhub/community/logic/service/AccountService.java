package com.prollhub.community.logic.service;

import com.prollhub.community.dto.auth.RegisterRequest;
import com.prollhub.community.persistency.model.Account;

import java.util.Optional;
import java.util.UUID;

public interface AccountService {

    /**
     * Registers a new user account based on the provided registration request.
     * Handles password hashing and validation.
     *
     * @param registerRequest DTO containing registration details.
     * @return The newly created Account object.
     * @throws RuntimeException (or specific exceptions) if registration fails (e.g., username/email exists).
     */
    Account registerNewAccount(RegisterRequest registerRequest);

    /**
     * Finds an account by its username.
     *
     * @param username The username to search for.
     * @return An Optional containing the found Account.
     */
    Optional<Account> findByUsername(String username);

    /**
     * Finds an account by its ID.
     *
     * @param id The UUID of the account.
     * @return An Optional containing the found Account.
     */
    Optional<Account> findById(UUID id);

}
