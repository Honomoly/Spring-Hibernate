package com.honomoly.study.hibernate.bean;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.honomoly.study.hibernate.annotation.TokenUser;
import com.honomoly.study.hibernate.exception.HttpException;
import com.honomoly.study.hibernate.util.JWT;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class TokenUserArgumentResolver implements HandlerMethodArgumentResolver {

    // long계열의 타입인지만 따로 확인
    private static boolean typeAvailability(Class<?> clazz) {
        return clazz == long.class || clazz == Long.class;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(TokenUser.class)
            && typeAvailability(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(
        MethodParameter parameter,
        ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest,
        WebDataBinderFactory binderFactory
    ) throws Exception {
        if (!(webRequest.getNativeRequest() instanceof HttpServletRequest request))
            throw new HttpException(HttpStatus.BAD_REQUEST, "Invalid request.");

        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer "))
            throw new HttpException(HttpStatus.PRECONDITION_FAILED, "Invalid Authorization.");

        String token = authorization.substring(7);

        try {
            return JWT.verifyJwt(token);
        } catch (JwtException e) {
            throw new HttpException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

}
