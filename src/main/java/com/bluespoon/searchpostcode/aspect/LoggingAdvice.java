package com.bluespoon.searchpostcode.aspect;

import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.bluespoon.searchpostcode.model.PostcodeAddress;

@Aspect
@Component
public class LoggingAdvice {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAdvice.class);

    @Before("com.bluespoon.searchpostcode.aspect.LoggingPointcut.traceMethod()")
    public void methodEntry(JoinPoint joinPoint){
        LOGGER.debug(">>>>>> Entry >>>>>> :{}", joinPoint.getSignature().toLongString());
    }

    @After("com.bluespoon.searchpostcode.aspect.LoggingPointcut.traceMethod()")
    public void methodExit(JoinPoint joinPoint){
        LOGGER.debug("<<<<<< Exit <<<<<< :{}", joinPoint.getSignature().toLongString());
    }

    @AfterThrowing(pointcut = "com.bluespoon.searchpostcode.aspect.LoggingPointcut.exceptionPoint()",
    throwing = "ex")
    public void printTrace(JoinPoint joinPoint, Exception ex){
        LOGGER.info(">>>>>> Exception >>>>>> :{}", joinPoint.getSignature().toLongString(), ex);
        LOGGER.info("<<<<<< Exception <<<<<< :");
    }

    @Before("com.bluespoon.searchpostcode.aspect.LoggingPointcut.searchPostcodePoint()")
    public void beforeSearchPostcodePoint(JoinPoint joinPoint){
        String arg = (String)joinPoint.getArgs()[0];
        LOGGER.info("{} 検索 :{}", joinPoint.getSignature().getName(), arg);
    }

    @AfterReturning(value = "com.bluespoon.searchpostcode.aspect.LoggingPointcut.searchPostcodePoint()", returning = "ret")
    public void AfterReturningSearchPostcodePoint(JoinPoint joinPoint, List<PostcodeAddress> ret){
        LOGGER.info("{} 件数 :{}", joinPoint.getSignature().getName(), ret.size());
    }


}
