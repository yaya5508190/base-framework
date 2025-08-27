package com.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebDemoController {

    @GetMapping("/api/parent")
    public String sayHello(){
        return "parent";
    }

    @GetMapping("/api/module-federation")
    public String getModuleFederation(){
        return "{\"remotes\":[{\"alias\":\"remote\",\"name\":\"remote\",\"entry\":\"http://localhost:8080/loader/943a6b1b-222a-4e99-b900-3744270480e6/mf-manifest.json\"}],\"menus\":[{\"name\":\"微应用页面\",\"path\":\"/remote\",\"component\":\"remote/App\"}]}";
    }

}
