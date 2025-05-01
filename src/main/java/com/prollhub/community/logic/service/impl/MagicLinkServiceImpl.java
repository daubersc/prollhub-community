package com.prollhub.community.logic.service.impl;

import com.prollhub.community.dto.auth.UserInfoDTO;
import com.prollhub.community.logic.service.EmailService;
import com.prollhub.community.logic.service.MagicLinkService;
import com.prollhub.community.logic.service.TokenService;
import com.prollhub.community.persistency.model.Account;
import com.prollhub.community.persistency.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.Locale;
import java.util.Optional; // Assuming repository returns Optional

// --- Assuming these dependencies exist ---
// import com.yourapp.repository.AccountRepository;
// import com.yourapp.model.Account;
// import com.yourapp.dto.UserInfoDTO;
// import com.yourapp.service.TokenService;
// import com.yourapp.service.EmailService;
// ---

/**
 * Implementation of the MagicLinkService interface.
 * Contains the asynchronous logic for processing magic link requests.
 */
@Service // Marks this class as a Spring service bean
public class MagicLinkServiceImpl implements MagicLinkService {

    private static final Logger log = LoggerFactory.getLogger(MagicLinkServiceImpl.class);

    // Use constructor injection (recommended) or @Autowired
    private final AccountRepository accountRepository;
    private final TokenService tokenService;
    private final EmailService emailService;

    @Autowired // Or better, use a constructor
    public MagicLinkServiceImpl(AccountRepository accountRepository,
                                TokenService tokenService,
                                EmailService emailService) {
        this.accountRepository = accountRepository;
        this.tokenService = tokenService;
        this.emailService = emailService;
    }

    @Override
    @Async // Apply the @Async annotation to the implementation method
    public void processMagicLinkRequest(String email, Locale locale) {
        log.info("Asynchronously processing magic link request for potential email: {}", email);
        try {
            // --- Background logic moved here ---
            Optional<Account> accountOptional = accountRepository.findByEmail(email); // Use Optional

            if (accountOptional.isPresent()) {
                Account account = accountOptional.get();
                log.info("Account found for {}, generating token and sending email.", email);
                String token = tokenService.createMagicLinkToken(account);

                // Try-catch INSIDE the async method for email sending
                try {
                    // Use locale.getLanguage() or the full locale as needed
                    emailService.sendTemplateEmail(new UserInfoDTO(account), EmailService.TemplateType.MAGIC_LINK, locale.getLanguage(), token, true);
                    log.info("Magic link email potentially sent to {}", email);
                } catch (Exception e) {
                    // Log the error thoroughly, but DO NOT re-throw or return an error response.
                    log.error("Failed to send magic link email to {} during async processing: {}", email, e.getMessage(), e);
                    // Possible actions: Metrics update, alert, add to dead-letter queue
                }
            } else {
                // Account doesn't exist - do nothing further in the background.
                log.info("No account found for {} during async processing. No email sent.", email);
            }
            // --- End of background logic ---

        } catch (Exception e) {
            // Catch any other unexpected errors during the async task
            log.error("Unexpected error during async magic link processing for email {}: {}", email, e.getMessage(), e);
        }
    }

}