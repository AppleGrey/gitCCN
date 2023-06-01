package com.example.web_test.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Hello {
    @RequestMapping("/hello")
    public String hello() {
        System.out.println("Hello!");
        return "Hello";
    }
}
