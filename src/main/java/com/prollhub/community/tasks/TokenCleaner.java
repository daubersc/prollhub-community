package com.prollhub.community.tasks;

import com.prollhub.community.persistency.repository.InviteTokenRepository;
import com.prollhub.community.persistency.repository.MagicLinkTokenRepository;
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

    /**
     * This method runs daily at 3:00 AM to delete expired magic link tokens.
     */
    @Scheduled(cron = "0 0 3 * * ?")
    @Transactional
    public void cleanupExpiredTokens() {
        Instant now = Instant.now();
        log.info("Running scheduled task to clean up tokens expired before: {}", now);

        try {
            log(magicLinkTokenRepository.deleteByExpiresBefore(now));
            log(inviteTokenRepository.deleteByExpiresBefore(now));

        } catch (Exception e) {
            log.error("Error during token cleanup task", e);
        }
    }

    private void log(long deletedCount) {
        String message = (deletedCount > 0)
                ? String.format("Successfully deleted %d expired tokens.", deletedCount)
                : "No expired tokens found to delete.";
        log.info(message);
    }



}
