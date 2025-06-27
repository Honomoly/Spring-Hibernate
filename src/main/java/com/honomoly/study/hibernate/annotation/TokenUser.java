package com.honomoly.study.hibernate.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 이 어노테이션이 API 메소드의 파라미터 값에 붙는 경우,
 * 자동으로 해당 API에 대해 토큰 인증을 적용한다.
 * <p>그리고 토큰에서 반환된 userId를 해당 파라미터에 주입한다.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface TokenUser {
}
