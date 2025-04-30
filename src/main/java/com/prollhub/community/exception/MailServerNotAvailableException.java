package com.prollhub.community.exception;

public class MailServerNotAvailableException extends IllegalArgumentException {
    public MailServerNotAvailableException(String message) {
        super(message);
    }
}
