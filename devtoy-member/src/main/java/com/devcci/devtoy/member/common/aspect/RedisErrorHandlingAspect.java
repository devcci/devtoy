package com.devcci.devtoy.member.common.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class RedisErrorHandlingAspect {

    @Around("execution(* com.devcci.devtoy..*RedisTemplateService.*(..))")
    public Object handleRedisErrors(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (RedisConnectionFailureException | RedisSystemException | QueryTimeoutException e) {
            log.error("Redis 장애 발생: {}", e.getMessage());
            return null;
        }
    }
}
