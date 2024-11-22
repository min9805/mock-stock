package com.mock.investment.stock.global.infra.redis.aop;

import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {
    String key();
    long waitTime() default 5000L;
    long leaseTime() default 2000L;
}
