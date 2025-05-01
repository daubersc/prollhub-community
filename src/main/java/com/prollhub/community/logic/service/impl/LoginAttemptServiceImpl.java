package com.prollhub.community.logic.service.impl;

// Example using Caffeine Cache (add com.github.ben-manes.caffeine:caffeine)
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.prollhub.community.logic.service.LoginAttemptService;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
public class LoginAttemptServiceImpl implements LoginAttemptService {

    private final Cache<String, Integer> attemptsCache;

    public LoginAttemptServiceImpl() {
        // Cache entries expire 1 hour after last write
        this.attemptsCache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)
                .maximumSize(1000) // Max users/IPs to track in memory
                .build();
    }

    public void loginFailed(String key) {
        int attempts = attemptsCache.get(key, k -> 0); // Get current attempts, default to 0
        attempts++;
        attemptsCache.put(key, attempts);
    }

    public void loginSucceeded(String key) {
        attemptsCache.invalidate(key); // Clear attempts on successful login
    }


    public boolean isBlocked(String key) {
        Integer attempts = attemptsCache.getIfPresent(key);
        // Max failed attempts allowed
        int MAX_ATTEMPTS = 5;
        return attempts != null && attempts >= MAX_ATTEMPTS;
    }
}