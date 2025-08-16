package com.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PluginViewController {

    @GetMapping("/")
    public String index() {
        return "forward:/index.html";
    }
}
