package com.boris.spring.chapter4;

import org.springframework.stereotype.Component;

@Component
public class PerformanceImp implements Performance {

    public void perform() {
        System.out.println("perform running.....");
//        throw new RuntimeException();
    }

    public void performParam(Param param) {
        System.out.println("Name:" + param.getName() + "  age:" + param.getAge());
    }
}
