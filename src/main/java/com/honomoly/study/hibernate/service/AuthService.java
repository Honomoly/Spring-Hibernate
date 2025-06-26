package com.honomoly.study.hibernate.service;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.honomoly.study.hibernate.domain.entity.User;
import com.honomoly.study.hibernate.dto.auth.AuthOutput;
import com.honomoly.study.hibernate.dto.auth.SignInInput;
import com.honomoly.study.hibernate.dto.auth.SignUpInput;
import com.honomoly.study.hibernate.dto.common.UserMin;
import com.honomoly.study.hibernate.exception.HttpException;
import com.honomoly.study.hibernate.repository.UserRepository;
import com.honomoly.study.hibernate.util.JWT;
import com.honomoly.study.hibernate.util.SHA256;

@Service
public class AuthService {

    private final UserRepository userRepository;

    AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AuthOutput signUp(SignUpInput input) {
        Optional<User> result = userRepository.findByIdentifier(input.getIdentifier());
    
        if (result.isPresent())
            throw new HttpException(HttpStatus.CONFLICT, "Identifier '" + input.getIdentifier() + "' already exists.");

        User user = new User(
            input.getName(),
            input.getEmail(),
            input.getTimezone(),
            input.getIdentifier(),
            input.getPassword()
        );

        userRepository.saveAndFlush(user);

        String token = JWT.generateJWT(user);

        return new AuthOutput(UserMin.from(user), token);
    }

    public AuthOutput signIn(SignInInput input) {
        Optional<User> result = userRepository.findByIdentifier(input.getIdentifier());

        if (result.isEmpty())
            throw new HttpException(HttpStatus.UNAUTHORIZED, "Sign-In fails.");

        User user = result.get();

        if (!SHA256.isValid(input.getPassword(), user.getPasswordHash(), user.getHashSalt()))
            throw new HttpException(HttpStatus.UNAUTHORIZED, "Sign-In fails.");

        String token = JWT.generateJWT(user);

        return new AuthOutput(UserMin.from(user), token);
    }

}
