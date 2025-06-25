package com.honomoly.study.hibernate.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.OkHttpClient;

/** Bean 객체가 아니라 별개로 관리 */
public final class Inst {

    private Inst() {} // 인스턴스화 불가

    public static final OkHttpClient okHttpClient = new OkHttpClient();
    
    public static final ObjectMapper objectMapper = new ObjectMapper();

}
