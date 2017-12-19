package com.boris.spring.chapter4;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PerformanceImp implements Performance {

    public void perform() {
        System.out.println("perform running.....");
//        throw new RuntimeException();
    }

    public void performParam(Param param) {
        param.setName("BORIS```");
        System.out.println("Name:" + param.getName() + "  age:" + param.getAge());

        List<String> lists = new ArrayList<String>();
        lists.get(2);
    }
}
