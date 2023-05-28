package com.bluespoon.searchpostcode.configuration;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bluespoon.searchpostcode.filter.CustomizedOncePerRequestFilter;

@Configuration
public class FilterConfiguration {

    @Bean
    public FilterRegistrationBean<?> CustomizedOncePerRequestFilter() {
        FilterRegistrationBean<CustomizedOncePerRequestFilter> frb = new FilterRegistrationBean<>(new CustomizedOncePerRequestFilter());
        frb.setOrder(Integer.MIN_VALUE);
        frb.addUrlPatterns("/*");
        return frb;
    }
}