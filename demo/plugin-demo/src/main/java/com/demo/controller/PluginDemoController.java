package com.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PluginDemoController {

    @GetMapping("/api/plugin")
    public String sayHello(){
        return "plugin";
    }

}
