package com.prollhub.community.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
public class ErrorResponse {

    private final String category = "ERROR";
    private final String code;
    private final int status;
    private final String message;
    private final Instant timestamp;

    public ErrorResponse(ErrorCode messageCode, String customMessage) {
        this.code = messageCode.name();
        this.status = messageCode.getHttpStatus().value();
        this.message = (customMessage != null && !customMessage.isBlank()) ? customMessage : messageCode.getDefaultMessage();
        this.timestamp = Instant.now();
    }

    public ErrorResponse(ErrorCode messageCode) {
        this(messageCode, null);
    }

}
