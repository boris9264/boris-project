package com.boris.controller;

import com.boris.common.exception.BorisException;
import com.boris.common.utils.JsonUtil;
import com.boris.common.vo.PageParamVo;
import com.boris.common.vo.ResponseVo;
import com.boris.model.User;
import com.boris.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Api(description="boris demo controller")
@Controller
@RequestMapping("/test")
@Slf4j
public class TestController {
    @Autowired
    private UserService userService;

    @ApiOperation(value="分页查询用户信息", notes="分页查询用户信息", httpMethod = "POST")
    @RequestMapping("/hello")
    @ResponseBody
    public ResponseVo<User> hello(@RequestBody PageParamVo param) {
        log.info("入参:{}", JsonUtil.toString(param));
        return userService.queryUser();
    }

    @ApiOperation(value="保存用户信息", notes="保存用户信息", httpMethod = "POST")
    @RequestMapping("/save")
    @ResponseBody
    public ResponseVo save(@RequestBody User user) throws BorisException {
        return userService.insert(user);
    }

    @ApiOperation(value="获取用户详情", notes="获取用户详情", httpMethod = "GET")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public ResponseVo<User> get(@ApiParam(name = "userId", value = "用户ID")
                                @RequestParam(value = "userId") Integer userId) {
        ResponseVo<User> responseVo = new ResponseVo<>();
        User user = new User();
        user.setId(userId);
        user.setName("boris");
        user.setGender(1);
        responseVo.setData(user);
        return responseVo;
    }

}
