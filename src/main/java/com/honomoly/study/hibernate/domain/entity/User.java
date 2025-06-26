package com.honomoly.study.hibernate.domain.entity;

import com.honomoly.study.hibernate.domain.BaseEntity;
import com.honomoly.study.hibernate.util.Randomizer;
import com.honomoly.study.hibernate.util.SHA256;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String timezone;

    @Column(unique = true, nullable = false)
    private String identifier;

    @Column(name = "hash_salt", columnDefinition = "BINARY(8)", nullable = false)
    private byte[] hashSalt; // 8 Byte 제한

    @Column(name = "password_hash", columnDefinition = "BINARY(32)", nullable = false)
    private byte[] passwordHash; // SHA-256 기반으로 생성 (32 Byte)

    // 유저 최초 생성(회원가입)시 사용되는 생성자
    public User(String name, String email, String timezone, String identifier, String password) {
        this.name = name;
        this.email = email;
        this.timezone = timezone;
        this.identifier = identifier;
        this.hashSalt = Randomizer.getRandomBytes(8); // 8 Byte 제한
        this.passwordHash = SHA256.getHash(password, this.hashSalt);
    }

}
