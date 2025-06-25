package com.honomoly.study.hibernate.bean;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.honomoly.study.hibernate.exception.HttpException;

/** API 관련 전역 예외처리 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpException.class)
    public ResponseEntity<HttpException.Body> httpExceptionHandler(HttpException httpException) {
        return ResponseEntity.status(httpException.getHttpStatus())
                .body(httpException.getBody());
    }

}
