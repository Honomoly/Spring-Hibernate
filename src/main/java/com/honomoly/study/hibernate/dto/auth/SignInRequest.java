package com.honomoly.study.hibernate.dto.auth;

import com.honomoly.study.hibernate.dto.RequestDto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignInRequest implements RequestDto {

    @NotBlank
    private final String identifier;

    @NotBlank
    private final String password;

}
