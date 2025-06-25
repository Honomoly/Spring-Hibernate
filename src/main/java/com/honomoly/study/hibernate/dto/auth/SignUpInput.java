package com.honomoly.study.hibernate.dto.auth;

import org.springframework.lang.NonNull;

import com.honomoly.study.hibernate.dto.InputDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignUpInput implements InputDto {

    private final String name;

    private final String email;

    private final String timezone;

    private final String identifier;

    private final String password;

    public static SignUpInput from(@NonNull SignUpRequest request) {
        return new SignUpInput(
            request.getName(),
            request.getEmail(),
            request.getTimezone(),
            request.getIdentifier(),
            request.getPassword()
        );
    }

}
