package com.honomoly.study.hibernate.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.honomoly.study.hibernate.bean.LoggingInterceptor;
import com.honomoly.study.hibernate.bean.TokenUserArgumentResolver;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final LoggingInterceptor loggingInterceptor;

    private final TokenUserArgumentResolver tokenUserArgumentResolver;

    WebMvcConfig(
        LoggingInterceptor loggingInterceptor,
        TokenUserArgumentResolver tokenUserArgumentResolver
    ) {
        this.loggingInterceptor = loggingInterceptor;
        this.tokenUserArgumentResolver = tokenUserArgumentResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggingInterceptor);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(tokenUserArgumentResolver);
    }

}
