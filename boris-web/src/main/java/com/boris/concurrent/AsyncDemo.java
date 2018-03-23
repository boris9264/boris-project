package com.boris.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AsyncDemo {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Async
    public void logOut() {

        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOG.info("init..........");
    }
}
