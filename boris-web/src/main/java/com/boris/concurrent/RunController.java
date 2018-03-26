package com.boris.concurrent;

import com.boris.common.utils.JsonUtil;
import com.boris.common.vo.PageParamVo;
import com.boris.common.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Map;

@Controller
public class RunController {
    @Autowired
    private AsyncDemo asyncDemo;

    @Autowired
    private PriceServiceImpl priceService;

    @RequestMapping("/asyncMaxRun")
    @ResponseBody
    public ResponseVo hello() {

        for (int i = 0; i <1000 ; i++) {
            asyncDemo.logOut();
        }
        return new ResponseVo();
    }

    @RequestMapping("/getPrice")
    @ResponseBody
    public DeferredResult<Map<String,Object>> getPrice() {
        DeferredResult<Map<String,Object>> deferredResult =priceService.getDeferredResult();
        if (deferredResult.getResult() == null) {
            System.out.println("fail...");
        }
        deferredResult.onTimeout(new Runnable() {
            @Override
            public void run() {
                deferredResult.setErrorResult("超时了...");
            }
        });

        deferredResult.onCompletion(new Runnable() {
            @Override
            public void run() {
                System.out.println(JsonUtil.toString(deferredResult.getResult()));
            }
        });
        return deferredResult;
    }
}
