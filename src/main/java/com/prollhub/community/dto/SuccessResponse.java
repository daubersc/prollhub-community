package com.prollhub.community.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Getter
public class SuccessResponse<T> {

    private final String category = "SUCCESS";
    private final String code;
    private final int status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String message;

    private final Instant timestamp;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T data;

    public SuccessResponse(HttpStatus status, String messageCode, String message, T data) {
        this.status = status.value();
        this.code = (messageCode != null && !message.isBlank()) ? messageCode : status.name();
        this.message = message;
        this.data = data;
        this.timestamp = Instant.now();
    }

    public SuccessResponse(HttpStatus status,  String message, T data) {
        this(status, null, message, data);
    }

    public SuccessResponse(HttpStatus status,  T data) {
        this(status, null, null, data);
    }


    public SuccessResponse(T data) {
        this(HttpStatus.OK, data);
    }

}
