package com.example.web_test.controller;


import com.example.web_test.pojo.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class Hello {
    @RequestMapping("/hello")
    public Result hello() {
        System.out.println("Hello!");
        List<String> l = new ArrayList<>();
        l.add("aa");
        l.add("qq");
        return Result.success(l);
    }
}
