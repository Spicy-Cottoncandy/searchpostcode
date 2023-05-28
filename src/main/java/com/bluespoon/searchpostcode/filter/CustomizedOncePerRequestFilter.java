package com.bluespoon.searchpostcode.filter;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomizedOncePerRequestFilter extends OncePerRequestFilter {

    //MDC 
    private static final String MDC_REQUEST_ID = "requestId";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        setMdc(request);

        filterChain.doFilter(request, response);

        //初期化
        MDC.clear();
    }


    public void setMdc(HttpServletRequest request){
        //初期化
        MDC.clear();

        //MDC設定
        MDC.put(MDC_REQUEST_ID, UUID.randomUUID().toString());
    }
}
