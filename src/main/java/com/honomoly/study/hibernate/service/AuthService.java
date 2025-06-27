package com.honomoly.study.hibernate.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.honomoly.study.hibernate.domain.entity.User;
import com.honomoly.study.hibernate.dto.auth.AuthOutput;
import com.honomoly.study.hibernate.dto.auth.SignInInput;
import com.honomoly.study.hibernate.dto.auth.SignUpInput;
import com.honomoly.study.hibernate.dto.common.UserMin;
import com.honomoly.study.hibernate.exception.HttpException;
import com.honomoly.study.hibernate.util.JWT;
import com.honomoly.study.hibernate.util.SHA256;

@Service
public class AuthService {

    private final UserService userService;

    AuthService(UserService userService) {
        this.userService = userService;
    }

    public AuthOutput signUp(SignUpInput input) {
        User user = userService.signUp(input);

        String token = JWT.generateJWT(user.getId());

        return new AuthOutput(UserMin.from(user), token);
    }

    public AuthOutput signIn(SignInInput input) {
        User user = userService.findByIdentifier(input.getIdentifier());

        if (!SHA256.isValid(input.getPassword(), user.getPasswordHash(), user.getHashSalt()))
            throw new HttpException(HttpStatus.UNAUTHORIZED, "Sign-In fails.");

        String token = JWT.generateJWT(user.getId());

        return new AuthOutput(UserMin.from(user), token);
    }

}
