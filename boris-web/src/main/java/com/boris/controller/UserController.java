package com.boris.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @RequestMapping("/{id}")
    public String view(@PathVariable("id") Long id) {
        System.out.println(id);
        return id.toString();
    }
}
