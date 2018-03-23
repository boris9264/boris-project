package com.boris.concurrent;

import com.boris.common.vo.PageParamVo;
import com.boris.common.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RunController {
    @Autowired
    private AsyncDemo asyncDemo;

    @RequestMapping("/asyncMaxRun")
    @ResponseBody
    public ResponseVo hello() {

        for (int i = 0; i <1000 ; i++) {
            asyncDemo.logOut();
        }
        return new ResponseVo();
    }
}
