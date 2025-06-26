package com.honomoly.study.hibernate.dto.auth;

import com.honomoly.study.hibernate.dto.RequestDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignUpRequest implements RequestDto {

    @NotBlank
    private final String name;

    @Email
    private final String email;

    @NotBlank
    private final String timezone;

    @NotBlank
    private final String identifier;

    @NotBlank
    private final String password;

}
