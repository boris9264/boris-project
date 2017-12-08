package com.boris.chapter3;

import com.boris.pojo.Apple;
import com.boris.util.JsonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class PredicateTest {

    public static void main(String[] args) {
        List<Apple> list = new ArrayList<>();
        Apple apple = new Apple();
        apple.setWeight(100);
        list.add(apple);

        Apple apple1 = new Apple();
        apple1.setWeight(110);
        list.add(apple1);

        Apple apple2 = new Apple();
        apple2.setWeight(80);
        list.add(apple2);

        List<Apple> results = PredicateTest.getResult(list,a -> a.getWeight()>80 && a.getWeight()>90, a-> a.setColor("RED"));

        System.out.println(JsonUtil.toString(results));
    }

    public static <T> List<T> getResult(List<T> list, Predicate<T> p, Consumer<T> c) {
        List<T> results = new ArrayList<>();
        for (T t : list) {
            if (p.test(t)) {
                results.add(t);
                c.accept(t);
            }
        }
        return results;
    }
}
