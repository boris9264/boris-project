package com.boris.concurrent;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PriceServiceImpl extends PriceService {

    @Override
    public Map<String,Object> getResult() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Map<String,Object> map = new HashMap<>();
        map.put("price","123.00");
        return map;
    }
}
