package com.boris.chapter2;

import org.junit.Test;

public class RunnableDemo {

    public static void main(String[] args) {
        Thread t = new Thread(new Runnable() {
            public void run() {
                System.out.println("thread running...");
            }
        });
        t.start();

        Thread t1 = new Thread(() -> System.out.println("java8 thread running..."));
        t1.start();

        int i = 256;
        Thread t2 = new Thread(() -> System.out.println("i=" + i));
        //i=1; lambda引用的局部变量必须是final
        t2.start();
    }
}
