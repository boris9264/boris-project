package com.boris.spring.chapter5;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    @RequestMapping(value = "/home")
    @ResponseBody
    public String home(@RequestParam(value="max") long max, @RequestParam(value="count") int count) {
        System.out.println(max);
        System.out.println(count);
        return "home";
    }

    @RequestMapping(value="/spittles/{spittleId}")
    @ResponseBody
    public String spittle(@PathVariable("spittleId") long spittleId) {
        System.out.println(spittleId);
        return "spittle";
    }

    @RequestMapping(value="/regist")
    @ResponseBody
    public String regist(Spitter spitter) {
        System.out.println(spitter.getName());
        System.out.println(spitter.getPassword());
        return "success";
    }
}
