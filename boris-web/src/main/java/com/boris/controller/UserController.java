package com.boris.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {
    @RequestMapping("/{id}")
    @ResponseBody
    public String view(@PathVariable("id") Long id) {
        System.out.println(id);
        return id.toString();
    }
}
