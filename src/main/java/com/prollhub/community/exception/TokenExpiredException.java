package com.prollhub.community.exception;

public class TokenExpiredException extends IllegalStateException {
    public TokenExpiredException(String message) {
        super(message);
    }
}
