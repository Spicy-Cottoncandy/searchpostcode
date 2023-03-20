package com.bluespoon.searchpostcode.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class LoggingPointcut {
    @Pointcut("execution(* com.bluespoon.searchpostcode..*(..))")
    public void traceMethod() {};

    @Pointcut("execution(* com.bluespoon.searchpostcode..*(..))")
    public void exceptionPoint() {};

    @Pointcut("execution(* com.bluespoon.searchpostcode.service.impl.GetPostcodeServiceImpl.searchPostcode(..))")
    public void searchPostcodePoint() {};

}
