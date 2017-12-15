package com.boris.controller;

import com.boris.spring.chapter4.Performance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private Performance performance;

    @RequestMapping("/{id}")
    @ResponseBody
    public String view(@PathVariable("id") Long id) {
        performance.perform();
        System.out.println(id);
        return id.toString();
    }
}
