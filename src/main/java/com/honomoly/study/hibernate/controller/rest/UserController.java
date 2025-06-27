package com.honomoly.study.hibernate.controller.rest;

import org.springframework.web.bind.annotation.RestController;

import com.honomoly.study.hibernate.annotation.TokenUser;
import com.honomoly.study.hibernate.dto.common.UserMin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping(("/api/rest/user"))
public class UserController {

    @GetMapping("")
    public ResponseEntity<UserMin> getUser(@TokenUser long userId) {

        // TODO: 유저 조회로직 작성

        return null;
    }

}
