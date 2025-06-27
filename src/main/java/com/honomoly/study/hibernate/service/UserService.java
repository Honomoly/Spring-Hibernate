package com.honomoly.study.hibernate.service;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.honomoly.study.hibernate.domain.entity.User;
import com.honomoly.study.hibernate.dto.auth.SignUpInput;
import com.honomoly.study.hibernate.exception.HttpException;
import com.honomoly.study.hibernate.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User signUp(SignUpInput input) {
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

        return userRepository.saveAndFlush(user);
    }

    public User findByIdentifier(String identifier) {
        Optional<User> result = userRepository.findByIdentifier(identifier);

        if (result.isEmpty())
            throw new HttpException(HttpStatus.UNAUTHORIZED, "Sign-In fails.");

        return result.get();
    }

}
