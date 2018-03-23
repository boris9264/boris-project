package com.boris.concurrent;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.Map;

@RestController
public class DeferredController {
    private DeferredResult<Map<String,Object>> deferredResult;

    //第一次浏览器请求testDeferredResult 浏览器会一直等待testDeferredResult的返回结果
    @RequestMapping("/testDeferredResult")
    public DeferredResult<Map<String,Object>> getDeferredResult() {
        deferredResult = new DeferredResult<>(5000L);
        deferredResult.onTimeout(new Runnable() {
            @Override
            public void run() {
                deferredResult.setErrorResult("超时.....");
            }
        });
        return deferredResult;
    }

    @RequestMapping("/setDeferredResult")
    public String setDeferredResult() {
        Map<String,Object> map = new HashMap<>();
        map.put("result","Test result!");
        deferredResult.setResult(map);
        return "succeed";
    }
}
