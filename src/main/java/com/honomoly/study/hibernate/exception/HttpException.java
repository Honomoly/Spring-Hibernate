package com.honomoly.study.hibernate.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class HttpException extends RuntimeException {

    private final HttpStatus httpStatus;

    private final String detail;

    public HttpException(HttpStatus httpStatus, String detail) {
        super(detail);
        this.httpStatus = httpStatus;
        this.detail = detail;
    }

    public Body getBody() {
        return new Body(
            httpStatus.value(),
            httpStatus.getReasonPhrase(),
            detail
        );
    }

    public record Body(
        int status,
        String title,
        String detail
    ) {}

}
