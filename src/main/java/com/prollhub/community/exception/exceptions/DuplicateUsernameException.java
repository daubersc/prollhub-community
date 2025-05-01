package com.prollhub.community.exception.exceptions;

public class DuplicateUsernameException extends IllegalArgumentException {
    public DuplicateUsernameException(String message) {
        super(message);
    }
}
