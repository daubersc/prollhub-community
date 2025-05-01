package com.prollhub.community.exception;

public class AccountDisabledException extends IllegalStateException {
    public AccountDisabledException(String message) {
        super(message);
    }
}
