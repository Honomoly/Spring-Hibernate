package com.honomoly.study.hibernate.dto.common;

import java.time.ZoneId;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.honomoly.study.hibernate.domain.entity.User;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/** 필수적인 유저정보만 추출한 DTO (User Minimized) */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMin {

    private final Long id;

    private final String name;

    private final String email;

    private final String timezone;

    private final String identifier;

    @JsonIgnore
    private ZoneId zoneId;

    public static UserMin from(User user) {
        UserMin userMin = new UserMin(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getTimezone(),
            user.getIdentifier()
        );

        userMin.zoneId = ZoneId.of(userMin.timezone);

        return userMin;
    }

}
