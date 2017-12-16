package com.boris.spring.chapter4;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class AudienceWithParam {

    @Pointcut("execution(* com.boris.spring.chapter4.Performance.performParam(..))" + " && args(param)")
    public void performParam(Param param) {}

    @Around("performParam(param)")
    public void beforPerform(ProceedingJoinPoint jp, Param param) {
        try {
            System.out.println(param.getName() + " Silencing cell phones");
            System.out.println("Talking seats");
            jp.proceed();
            System.out.println("CLAP CLAP CLAP!!!");
        } catch (Throwable throwable) {
            System.out.println("Demanding a refund");
        }
    }
}
