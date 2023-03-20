package com.bluespoon.searchpostcode.controller.custom;

import org.springframework.test.web.servlet.DispatcherServletCustomizer;
import org.springframework.web.servlet.DispatcherServlet;

public class CustomizedDispatcherServletCustomizer implements DispatcherServletCustomizer{

    @Override
    public void customize(DispatcherServlet dispatcherServlet){
        dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
    }
}
