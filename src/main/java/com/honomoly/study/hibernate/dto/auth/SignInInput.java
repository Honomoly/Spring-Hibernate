package com.honomoly.study.hibernate.dto.auth;

import com.honomoly.study.hibernate.dto.InputDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignInInput implements InputDto {

    private final String identifier;

    private final String password;

    public static SignInInput from(SignInRequest request) {
        return new SignInInput(
            request.getIdentifier(),
            request.getPassword()
        );
    }

}
