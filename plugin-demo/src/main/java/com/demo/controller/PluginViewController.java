package com.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PluginViewController {

    @GetMapping("/a")
    public String index() {
        return "forward:/plugin/a.html";
    }

    @GetMapping("/b")
    public String a() {
        return "forward:/plugin/b.html";
    }

    @GetMapping("/c")
    public String b() {
        return "forward:/plugin/c.html";
    }
}
