package com.boris.spring.chapter4;

import org.aspectj.lang.annotation.*;

@Aspect
public class Audience {
    //避免频繁使用切点表达式
    @Pointcut("execution(* com.boris.spring.*.*.perform(..))")
    public void performance(){}

    @Before("performance()")
    public void silenceCellPhones(){
        System.out.println("Silencing cell phones");
    }
    @Before("performance()")
    public void taskSeats(){
        System.out.println("Talking seats");
    }
    @AfterReturning("performance()")
    public void applause(){
        System.out.println("CLAP CLAP CLAP!!!");
    }
    @AfterThrowing("performance()")
    public void demanRefund(){
        System.out.println("Demanding a refund");
    }
}
