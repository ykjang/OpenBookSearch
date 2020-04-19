package com.yorath.booksearch.common;


import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

/**
 * 비동기 처리의 예외발생 핸들러
 */
@Slf4j
public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {

        log.error("Aysnc Exception Massage:" + ex.getMessage());
        log.error("Async Method Name: " + method.getName());
        for (Object param : params) {
            log.error("Method Param:" + param);
        }

    }
}
