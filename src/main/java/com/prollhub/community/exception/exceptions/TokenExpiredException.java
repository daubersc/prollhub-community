package com.prollhub.community.exception.exceptions;

public class TokenExpiredException extends IllegalStateException {
    public TokenExpiredException(String message) {
        super(message);
    }
}
