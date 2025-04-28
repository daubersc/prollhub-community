package com.prollhub.community.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Getter
public class WarningResponse<T> {

    private final String category = "WARNING";
    private final String code;
    private final int status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String message;

    private final Instant timestamp;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T data;

    public WarningResponse(HttpStatus status, String messageCode, String message, T data) {
        this.status = status.value();
        this.code = (messageCode != null && !message.isBlank()) ? messageCode : status.name();
        this.message = message;
        this.data = data;
        this.timestamp = Instant.now();
    }

    public WarningResponse(HttpStatus status, String message, T data) {
        this(status, null, message, data);
    }

    public WarningResponse(String message, T data) {
        this(HttpStatus.OK, message, data);
    }


}
