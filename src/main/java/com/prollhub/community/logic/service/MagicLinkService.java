package com.prollhub.community.logic.service;

import java.util.Locale;

/**
 * Interface defining the contract for handling magic link requests.
 */
public interface MagicLinkService {

    /**
     * Processes a magic link request asynchronously.
     * Implementations should handle finding the user, generating a token,
     * sending the email, and logging appropriately, without revealing
     * account existence via timing or direct error propagation to the original caller.
     *
     * @param email The email address provided in the request.
     * @param locale The locale resolved from the request, for localization purposes (e.g., email template).
     */
    void processMagicLinkRequest(String email, Locale locale);

}