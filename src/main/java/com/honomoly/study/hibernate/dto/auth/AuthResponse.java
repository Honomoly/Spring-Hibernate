package com.honomoly.study.hibernate.dto.auth;

import org.springframework.lang.NonNull;

import com.honomoly.study.hibernate.dto.ResponseDto;
import com.honomoly.study.hibernate.dto.common.UserMin;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse implements ResponseDto {

    private final UserMin user;

    private final String accessToken;

    public static AuthResponse from(@NonNull AuthOutput output) {
        return new AuthResponse(
            output.getUser(),
            output.getAccessToken()
        );
    }

}
