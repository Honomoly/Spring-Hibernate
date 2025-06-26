package com.honomoly.study.hibernate.dto.auth;

import com.honomoly.study.hibernate.dto.OutputDto;
import com.honomoly.study.hibernate.dto.common.UserMin;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthOutput implements OutputDto {

    private final UserMin user;

    private final String accessToken;

}
