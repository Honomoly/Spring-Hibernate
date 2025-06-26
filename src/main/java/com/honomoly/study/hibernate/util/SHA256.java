package com.honomoly.study.hibernate.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class SHA256 {

    private SHA256() {}

    /**
     * 매 실행시에 새로운 다이제스트 객체 생성
     * @return MessageDigest
     */
    private static MessageDigest getSHA256Digest() {
        try {
            return MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            // SHA-256 알고리즘이 없을리가...
            throw new RuntimeException(e);
        }
    }

    /**
     * 해당 입력값으로부터 해시값을 계산
     * @param input
     * @return byte[32]
     */
    public static byte[] getHash(String input) {
        return getHash(input, null);
    }

    /**
     * 텍스트 앞에 해당 랜덤솔트값을 붙이고 해시값을 계산
     * @param input
     * @param salt
     * @return byte[32]
     */
    public static byte[] getHash(String input, byte[] salt) {

        MessageDigest digest = getSHA256Digest();

        if (salt != null)
            digest.update(salt);

        return digest.digest(input.getBytes(StandardCharsets.UTF_8));

    }

    /**
     * 입력값으로부터 계산한 해시값이 결과 해시값과 동일한지 비교 연산
     * @param input
     * @param target
     * @return boolean
     */
    public static boolean isValid(String input, byte[] target) {
        return isValid(input, target, null);
    }

    /**
     * 입력값의 앞에 salt에 해당하는 문자열을 붙인 후에 비교 연산
     * @param input
     * @param target
     * @param salt
     * @return boolean
     */
    public static boolean isValid(String input, byte[] target, byte[] salt) {
        return Arrays.equals(target, getHash(input, salt));
    }

}
