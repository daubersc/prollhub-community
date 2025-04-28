package com.prollhub.community.logic.security;

import com.prollhub.community.persistency.repository.AccountRepository;
import lombok.RequiredArgsConstructor; // Using Lombok for constructor injection
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Optional but good practice

/**
 * Implementation of Spring Security's UserDetailsService.
 * Loads user-specific data from the database using AccountRepository.
 */
@Service // Marks this class as a Spring service component
@RequiredArgsConstructor // Lombok annotation to generate constructor for final fields
public class UserDetailsServiceImpl implements UserDetailsService {

    // Inject the AccountRepository dependency
    private final AccountRepository accountRepository;

    /**
     * Locates the user based on the username. In the actual implementation, the search
     * may possibly be case sensitive depending on how the implementor configured their
     * data store.
     *
     * @param username the username identifying the user whose data is required.
     * @return a fully populated user record (never null)
     * @throws UsernameNotFoundException if the user could not be found or the user has no
     * GrantedAuthority
     */
    @Override
    @Transactional(readOnly = true) // Optional: Marks the transaction as read-only for performance
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Use the AccountRepository to find the account by username
        // The Account entity itself implements UserDetails
        return accountRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username: " + username)
                );
    }
}
