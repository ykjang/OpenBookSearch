package com.yorath.booksearch.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class ApiCommonInterceptor implements HandlerInterceptor {


    private long requestStartTimeMillis = 0L;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        requestStartTimeMillis = System.currentTimeMillis ( );
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        // 요청처리시간 로그처리
        log.info("[Request Host]-{}", request.getRemoteHost ());
        log.info("[Request Path]-{}", request.getRequestURI ());
        log.info("[ProcessTime]-{}ms", (System.currentTimeMillis () - requestStartTimeMillis)) ;
    }
}
