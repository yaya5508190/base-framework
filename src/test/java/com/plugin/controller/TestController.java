package com.plugin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {

    @PostMapping("/post")
    public String post() {
        return "postMapping";
    }

    @GetMapping("/get")
    public String get() {
        return "getMapping";
    }
}
