package com.prollhub.community.exception.exceptions;

public class MailServerNotAvailableException extends IllegalArgumentException {
    public MailServerNotAvailableException(String message) {
        super(message);
    }
}
