package com.hamdan.slinkapi.infra.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiErrorException extends RuntimeException {

    private final HttpStatus httpStatus;

    public ApiErrorException(HttpStatus httpStatus, String msg) {
        super(msg);
        this.httpStatus = httpStatus;
    }

    public ApiErrorException(HttpStatus httpStatus, EApiErrorMessage apiErrorMessage) {
        super(apiErrorMessage.MESSAGE);
        this.httpStatus = httpStatus;
    }

}
