package com.prollhub.community.persistency.repository;

import com.prollhub.community.persistency.model.Account;
import com.prollhub.community.persistency.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

/**
 * Spring Data JPA repository for the {@link Account} entity.
 * Provides CRUD operations and custom finder methods.
 */
@Repository // Optional: Marks this interface as a Spring bean (though JpaRepository implies it)
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, String> {

    /**
     * Finds an account by its username.
     * Spring Data JPA automatically implements this method based on its name.
     *
     * @param token the token to search for
     * @return An Optional containing the found Account, or empty if not found.
     */
    Optional<VerificationToken> findByToken(String token);


    /**
     * Checks if an account exists with the given username.
     * More efficient than fetching the whole entity if you only need to check existence.
     *
     * @param token The token to check.
     * @return true if an account with the username exists, false otherwise.
     */
    boolean existsByToken(String token);

    /**
     * Deletes all tokens that have expired before the given timestamp.
     *
     * @param now The timestamp indicating the current time. Tokens expiring before this will be deleted.
     * @return The number of tokens deleted
     */
    @Transactional
    long deleteByExpiresBefore(Instant now);

}
