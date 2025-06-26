package com.honomoly.study.hibernate.bean;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    // 요청 처리시간을 계산하기 위한 필드
    private static final ThreadLocal<Long> startTime = new ThreadLocal<>();

    // 메소드명은 노란색으로 강조하도록 설정
    private static final String LOGGING_FORMAT = coloring("33", "[%s]") + " %s %s (%dms)";

    /**
     * 로크 색칠용 함수
     * @param color
     * @param obj
     * @param others
     * @return
     */
    public static String coloring(String color, Object obj, Object... others) {
        StringBuilder sb = new StringBuilder(128)
                .append("\033[")
                .append(color)
                .append("m")
                .append(obj);

        for (int i = 0; i < others.length; i++)
            sb.append(others[i]);

        return sb.append("\033[0m").toString();
    }

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) {
        startTime.set(System.currentTimeMillis());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest req, HttpServletResponse res, Object handler, Exception e) {
        long timeInterval = System.currentTimeMillis() - startTime.get();
        startTime.remove();

        int httpStatusCode = res.getStatus();
        HttpStatus httpStatus;

        try {
            httpStatus = HttpStatus.valueOf(httpStatusCode);
        } catch (IllegalArgumentException _e) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        String color = switch (httpStatus.series()) {
            case INFORMATIONAL -> "37";
            case SUCCESSFUL -> "32";
            case REDIRECTION -> "34";
            case CLIENT_ERROR -> "91";
            case SERVER_ERROR -> "31";
        };

        String responseStr = coloring(color, httpStatusCode, " ", httpStatus.getReasonPhrase());

        String mainLog = String.format(LOGGING_FORMAT, req.getMethod(), req.getRequestURI(), responseStr, timeInterval);

        log.info(mainLog);
    }

}
