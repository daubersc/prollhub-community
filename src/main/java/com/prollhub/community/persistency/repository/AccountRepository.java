package com.prollhub.community.persistency.repository;

import com.prollhub.community.persistency.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for the {@link Account} entity.
 * Provides CRUD operations and custom finder methods.
 */
@Repository // Optional: Marks this interface as a Spring bean (though JpaRepository implies it)
public interface AccountRepository extends JpaRepository<Account, UUID> {

    /**
     * Finds an account by its username.
     * Spring Data JPA automatically implements this method based on its name.
     *
     * @param username The username to search for.
     * @return An Optional containing the found Account, or empty if not found.
     */
    Optional<Account> findByUsername(String username);


    /**
     * Finds an account by its email address.
     * Spring Data JPA automatically implements this method based on its name.
     *
     * @param email The email address to search for.
     * @return An Optional containing the found Account, or empty if not found.
     */
    Optional<Account> findByEmail(String email);

    /**
     * Checks if an account exists with the given username.
     * More efficient than fetching the whole entity if you only need to check existence.
     *
     * @param username The username to check.
     * @return true if an account with the username exists, false otherwise.
     */
    boolean existsByUsername(String username);

    /**
     * Checks if an account exists with the given email address.
     * More efficient than fetching the whole entity if you only need to check existence.
     *
     * @param email The email address to check.
     * @return true if an account with the email exists, false otherwise.
     */
    boolean existsByEmail(String email);

    // You can add more custom query methods here as needed, potentially using @Query annotation
    // for more complex JPQL or native SQL queries.
}
