package com.prollhub.community.exception;

import lombok.Getter;

import java.time.Instant;
import java.util.Map;

@Getter
public class ErrorResponse {

    private final String category = "ERROR";
    private final String code;
    private final int status;
    private final String message;
    private final Instant timestamp;
    private final Map<String, Object> parameters;

    public ErrorResponse(ErrorCode messageCode, String customMessage) {
        this.code = messageCode.name();
        this.status = messageCode.getHttpStatus().value();
        this.message = (customMessage != null && !customMessage.isBlank()) ? customMessage : messageCode.getDefaultMessage();
        this.timestamp = Instant.now();
        this.parameters = null;
    }

    public ErrorResponse(ErrorCode messageCode) {
        this.code = messageCode.name();
        this.status = messageCode.getHttpStatus().value();
        this.message = messageCode.getDefaultMessage();
        this.timestamp = Instant.now();
        this.parameters = null;
    }

    public ErrorResponse(ErrorCode messageCode, Map<String, Object> params) {
        this.code = messageCode.name();
        this.status = messageCode.getHttpStatus().value();
        this.timestamp = Instant.now();
        this.parameters = params;

        if (messageCode == ErrorCode.FORMAT_ERROR) {
            String message = messageCode.getDefaultMessage();
            String parameters = String.join(", ", params.keySet());
            this.message = message + " " + parameters;
        } else {
            this.message = messageCode.getDefaultMessage();
        }

    }

}
