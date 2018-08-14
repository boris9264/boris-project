package com.boris.controller;

import com.boris.common.exception.BorisException;
import com.boris.common.utils.JsonUtil;
import com.boris.common.vo.PageParamVo;
import com.boris.common.vo.ResponseVo;
import com.boris.model.User;
import com.boris.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/test")
public class TestController {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @RequestMapping("/hello")
    @ResponseBody
    public ResponseVo hello(@RequestBody PageParamVo param) {
        LOG.info("入参:{}", JsonUtil.toString(param));
        return userService.queryUser();
    }

    @RequestMapping("/save")
    @ResponseBody
    public ResponseVo save(@RequestBody User user) throws BorisException {
        return userService.insert(user);
    }
}
