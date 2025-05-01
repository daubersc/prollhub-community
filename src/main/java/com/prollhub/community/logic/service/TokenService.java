package com.prollhub.community.logic.service;

import com.prollhub.community.dto.auth.RegisterRequest;
import com.prollhub.community.persistency.model.Account;
import com.prollhub.community.persistency.model.InviteToken;
import com.prollhub.community.persistency.model.MagicLinkToken;
import com.prollhub.community.persistency.model.VerificationToken;

import java.util.Optional;
import java.util.UUID;

public interface TokenService {

    /**
     * Creates an InviteToken that is consumed on registration of a new Account with a ttl of 7 days.
     * @return The token's UUID.
     */
    String createInviteToken();

    /**
     * Gets an inviteToken by its UUID if it exists and is not expired.
     * @param token The token's UUID
     * @return the Invite Token
     */
    void isValidInviteToken(String token);

    /**
     * Eventually deletes an inviteToken
     * @param token The token's UUID
     */
    void deleteInviteToken(String token);


    /**
     * Creates a magic link token with a TTL of 15 mins that a user can use to login to their account.
     * @param account The account the token shall be created for
     * @return The Token's UUID
     */
    String createMagicLinkToken(Account account);

    /**
     * Gets the account from the magic link token if it exists and is not expired.
     * @param token The token's UUID
     * @return the acccount to login.
     */
    Account getAccountFromLinkToken(String token);

    /**
     * Eventually deletes a magic link token
     * @param token The token's UUID
     */
    void deleteLinkToken(String token);

    /**
     * Creates an email verification token with a ttl of 1 day that activates an account.
     * @param account The account which shall be verified.
     * @return The token's UUID
     */
    String createVerificationToken(Account account);

    /**
     * Gets the account from the verification token if the token exists and is still valid.
     * @param token the token's UUID
     * @return the account if it exists
     */
    Account getAccountFromVerificationToken(String token);

    /**
     * Eventually deletes an verification token
     * @param token The token's UUID
     */
    void deleteVerificationToken(String token);



}
