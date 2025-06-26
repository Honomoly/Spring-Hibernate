package com.honomoly.study.hibernate.controller;

import org.springframework.web.bind.annotation.RestController;

import com.honomoly.study.hibernate.dto.auth.SignUpInput;
import com.honomoly.study.hibernate.dto.auth.AuthOutput;
import com.honomoly.study.hibernate.dto.auth.SignUpRequest;
import com.honomoly.study.hibernate.dto.auth.AuthResponse;
import com.honomoly.study.hibernate.dto.auth.SignInInput;
import com.honomoly.study.hibernate.dto.auth.SignInRequest;
import com.honomoly.study.hibernate.exception.HttpException;
import com.honomoly.study.hibernate.service.AuthService;

import jakarta.validation.Valid;

import java.time.ZoneId;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<AuthResponse> signUp(@RequestBody @Valid SignUpRequest request) {
        try {
            ZoneId.of(request.getTimezone());
        } catch (Exception e) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Invalid timezone '" + request.getTimezone() + "'");
        }

        AuthOutput output = authService.signUp(SignUpInput.from(request));

        return ResponseEntity.ok(AuthResponse.from(output));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<AuthResponse> signIn(@RequestBody @Valid SignInRequest request) {
        
        AuthOutput output = authService.signIn(SignInInput.from(request));

        return ResponseEntity.ok(AuthResponse.from(output));
    }

}
