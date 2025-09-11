package com.demo;

import com.plugin.annotation.EnablePluginLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnablePluginLoader
public class SpringPluginWebDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringPluginWebDemoApplication.class, args);
    }

}
