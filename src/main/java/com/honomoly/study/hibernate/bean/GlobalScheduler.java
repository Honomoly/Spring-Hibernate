package com.honomoly.study.hibernate.bean;

import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.honomoly.study.hibernate.util.JWT;

import lombok.extern.slf4j.Slf4j;

/** 스케줄러 관리 */
@Component
@Slf4j
public class GlobalScheduler {

    @Scheduled(timeUnit = TimeUnit.DAYS, fixedRate = 16) // 16일에 한 번 실행
    public void updateCache() {
        JWT.updateCache();
        log.info("✅ Cache Updated Successfully.");
    }

}
