package com.boris.chapter1;

import com.boris.pojo.Apple;
import com.boris.util.JsonUtil;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class AppleFilter {
    private List<Apple> apples = new ArrayList<>();

    @Before
    public void init() {
        Apple apple = new Apple();
        apple.setColor("green");
        apple.setWeight(150);
        apples.add(apple);
        System.out.println(JsonUtil.toString(apples));
    }

    @Test
    public void run() {
        System.out.println("run...");
    }
}
