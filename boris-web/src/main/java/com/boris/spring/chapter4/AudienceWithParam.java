package com.boris.spring.chapter4;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

@Aspect
public class AudienceWithParam {

    @Pointcut("execution(* com.boris.spring.chapter4.Performance.performParam(Param)) && args(param)")
    public void performParam(Param param) {}

    @Around("performParam(param)")
    public void beforPerform(ProceedingJoinPoint jp, Param param) {
        try {
            System.out.println(param.getName() + " Silencing cell phones");
            System.out.println("Talking seats");
            jp.proceed();
            System.out.println(param.getName() + "CLAP CLAP CLAP!!!");
        } catch (Throwable throwable) {
            Writer result = new StringWriter();
            PrintWriter printWriter = new PrintWriter(result);
            throwable.printStackTrace(printWriter);
            System.out.println(result.toString());
            System.out.println("Demanding a refund");
        }
    }
}
