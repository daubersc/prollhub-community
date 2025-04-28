package com.prollhub.community.exception;

public class DuplicateUsernameException extends IllegalArgumentException {
    public DuplicateUsernameException(String message) {
        super(message);
    }
}
