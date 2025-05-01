package com.prollhub.community.exception.exceptions;

public class AccountDisabledException extends IllegalStateException {
    public AccountDisabledException(String message) {
        super(message);
    }
}
