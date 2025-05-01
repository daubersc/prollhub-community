package com.prollhub.community.tasks;

import com.prollhub.community.persistency.repository.InviteTokenRepository;
import com.prollhub.community.persistency.repository.MagicLinkTokenRepository;
import com.prollhub.community.persistency.repository.VerificationTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class TokenCleaner {

    private static final Logger log = LoggerFactory.getLogger(TokenCleaner.class);
    private final MagicLinkTokenRepository magicLinkTokenRepository;
    private final InviteTokenRepository inviteTokenRepository;
    private final VerificationTokenRepository verificationTokenRepository;

    /**
     * This method runs every 6 Hours to delete expired tokens.
     */
    @Scheduled(cron = "0 0 3 * * ?")
    @Transactional
    public void cleanupExpiredTokens() {
        Instant now = Instant.now();
        log.info("Running scheduled task to clean up tokens expired before: {}", now);

        try {
            log(magicLinkTokenRepository.deleteByExpiresBefore(now), "magic link");
            log(inviteTokenRepository.deleteByExpiresBefore(now), "invite");
            log(verificationTokenRepository.deleteByExpiresBefore(now), "verification");

        } catch (Exception e) {
            log.error("Error during token cleanup task", e);
        }
    }

    private void log(long deletedCount, String type) {
        String message = (deletedCount > 0)
                ? String.format("Successfully deleted %d expired %s tokens.", deletedCount, type)
                : String.format("No expired %s tokens found to delete.", type);
        log.info(message);
    }



}
