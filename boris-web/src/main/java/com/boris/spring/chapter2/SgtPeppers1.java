package com.boris.spring.chapter2;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class SgtPeppers1 implements CompactDisc {
    private String title = "22Sgt. Pepper's Lonely Hearts Club Band";
    private String artist = "22The Beatles";

    public void play() {
        System.out.println("Playing " + title + " by " + artist);
    }
}
