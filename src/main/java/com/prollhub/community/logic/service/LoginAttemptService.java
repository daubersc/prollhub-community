package com.prollhub.community.logic.service;

public interface LoginAttemptService {

    /**
     * Checks if a login attempt shall be blocked based on the given key
     * @param key The key to identify the attempt, i.e. username or ip address
     * @return {@code true} if blocked {@code false} else
     */
    public boolean isBlocked(String key);

    /**
     * handles the success logic after successful login for the given key. Typically this is invalidating the storage.
     * @param key The key to identify the attempt, i.e. username or ip address
     */
    public void loginSucceeded(String key);

    /**
     * Handles the logic after failed login attempts for the given key, i.e. increase the counter
     * @param key The key to identify the attempt, i.e. username or ip address
     */
    public void loginFailed(String key);
}
