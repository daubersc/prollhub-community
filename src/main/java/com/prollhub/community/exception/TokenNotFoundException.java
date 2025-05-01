package com.prollhub.community.exception;

public class TokenNotFoundException extends IllegalStateException {
    public TokenNotFoundException(String message) {
        super(message);
    }
}
