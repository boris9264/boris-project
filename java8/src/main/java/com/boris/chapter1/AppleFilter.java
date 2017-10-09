package com.boris.chapter1;

import com.boris.pojo.Apple;
import com.boris.util.JsonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Locale.filter;

public class AppleFilter {

    static List<Apple> filterApples(List<Apple> inventory, Predicate<Apple> p) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if (p.test(apple)) {
                result.add(apple);
            }
        }
        return result;
    }

    static List<Apple> filterApplesByPredicates(List<Apple> inventory, List<Predicate<Apple>> predicates) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            boolean flag = true;
            for (Predicate<Apple> predicate : predicates) {
                if (!predicate.test(apple)) {
                    flag = false;
                    break;
                }
            }

            if(flag) {
                result.add(apple);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        List<Apple> inventory = new ArrayList<>();
        Apple apple = new Apple();
        apple.setColor("green");
        apple.setWeight(150);
        inventory.add(apple);

        Apple apple1 = new Apple();
        apple1.setColor("green");
        apple1.setWeight(160);
        inventory.add(apple1);

        Apple apple2 = new Apple();
        apple2.setColor("red");
        apple2.setWeight(160);
        inventory.add(apple2);

//        List<Apple> apples = filterApples(inventory, Apple::isGreenApple);

//        List<Apple> apples = filterApples(inventory, (Apple a)-> "green".equals(a.getColor()));

        List<Predicate<Apple>> predicates = new ArrayList<>();
        predicates.add((Apple a) -> "green".equals(a.getColor()));
        predicates.add((Apple a) -> a.getWeight()>150);
        List<Apple> newApples = filterApplesByPredicates(inventory,predicates);
        System.out.println(JsonUtil.toString(newApples));

        List<Apple> apples = inventory.stream().filter((Apple a) -> "green".equals(a.getColor())).collect(Collectors.toList());

        System.out.println(JsonUtil.toString(apples));

        apples = filterApples(apples, Apple::isHeavyApple);
        System.out.println(JsonUtil.toString(apples));
    }
}
