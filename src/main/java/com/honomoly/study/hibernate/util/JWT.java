package com.honomoly.study.hibernate.util;

import java.io.IOException;
import java.security.Key;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.fasterxml.jackson.databind.JsonNode;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Locator;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.InvalidKeyException;

public class JWT {

    private JWT() {}

    /** 토큰 발급을 위한 키값을 캐싱하기 위한 클래스 */
    public record KeyCache(
        String id,
        SecretKey key,
        long expireMillis
    ) {}

    private static final long KEY_CACHE_LIFETIME = TimeUnit.DAYS.toMillis(31); // 키 수명은 31일

    private static final Map<String, KeyCache> keyCacheMap = new ConcurrentHashMap<>();
    
    private static final JwtParser jwtParser = Jwts.parser()
            .keyLocator(new Locator<Key>() {
                @Override
                public Key locate(Header header) {
                    if (header.get("kid") instanceof String kid) {
                        KeyCache keyCache = keyCacheMap.get(kid);
                        if (keyCache != null)
                            return keyCache.key();
                    }
                    throw new UnsupportedJwtException("Invalid token header - kid");
                }
            })
            .build();

    private static volatile KeyCache activatedKeyCache;

    /**
     * 새로운 키 발급 및 만료된 키값 제거
     */
    public static void updateCache() {

        final long now = System.currentTimeMillis();

        // 1. 만료된 키값 제거
        keyCacheMap.entrySet().removeIf(e -> e.getValue().expireMillis() < now);

        // 2. 새 키 발급
        // 무작위로 생성된 41바이트중 9바이트는 키ID, 32바이트는 비밀키 생성에 할당 
        byte[] randomBytes = Randomizer.getRandomBytes(41);

        byte[] keyIdBytes = new byte[9];
        byte[] secretKeyBytes = new byte[32];

        System.arraycopy(randomBytes, 0, keyIdBytes, 0, 9);
        System.arraycopy(randomBytes, 9, secretKeyBytes, 0, 32);

        String newKeyId = Base64.getUrlEncoder().encodeToString(keyIdBytes);
        SecretKey newSecretKey = new SecretKeySpec(secretKeyBytes, "HmacSHA256");

        KeyCache newPack = new KeyCache(
            newKeyId,
            newSecretKey,
            now + KEY_CACHE_LIFETIME
        );

        keyCacheMap.put(newKeyId, newPack);

        activatedKeyCache = newPack;
        
    }

    /**
     * 유저 정보로부터 토큰 생성, 만료시간은 2시간
     * @param user
     * @return
     */
    public static String generateJWT(long userId) {

        final Instant now = Instant.now();
        final KeyCache cache = activatedKeyCache;

        try {
            return Jwts.builder()
                    .header()
                    .keyId(cache.id())
                    .and()
                    .issuer("honomoly")
                    .subject("user-login")
                    .issuedAt(Date.from(now))
                    .expiration(Date.from(now.plusMillis(TimeUnit.HOURS.toMillis(2))))
                    .claim("userId", userId) // 유저ID 추가
                    .signWith(cache.key()) // 서명
                    .compact(); // 생성
        } catch (InvalidKeyException e) {
            throw new RuntimeException("Secret Key Generation Error.", e);
        }

    }

    /**
     * 입력받은 토큰값을 검증한 후에 유저ID값을 반환
     * @param token
     * @return userId
     * @throws JwtException : 토큰 검증 실패시
     */
    public static long verifyJwt(String token) {
        return switch (jwtParser.parse(token).getPayload()) {
            case byte[] bytes -> { // 바이너리 데이터인 경우, 수동 파싱 시도
                long userId;

                try {
                    JsonNode payload = Inst.objectMapper.readTree(bytes);
                    userId = payload.get("userId").asLong(-1);
                } catch (IOException e) {
                    throw new JwtException("Token Parsing Error", e);
                }

                if (userId == -1)
                    throw new UnsupportedJwtException("Missing token value - userId");

                yield userId;
            }
            case Claims claims -> claims.get("userId", Long.class);
            default -> throw new JwtException("Unknown Error");
        };
    }

}
