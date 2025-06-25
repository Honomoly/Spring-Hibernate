package com.honomoly.study.hibernate.util;

import java.security.SecureRandom;

public class Randomizer {

    private static final SecureRandom RANDOM = new SecureRandom();

    public static byte[] getRandomBytes(int length) {

        if (length < 1)
            throw new IllegalArgumentException("Length should be positive.");

        byte[] arr = new byte[length];

        RANDOM.nextBytes(arr);

        return arr;

    }

}
