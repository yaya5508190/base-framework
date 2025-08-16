package com.demo;

import com.plugin.annotation.EnablePluginLoadServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnablePluginLoadServer
public class SpringPluginDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringPluginDemoApplication.class, args);
    }

}
