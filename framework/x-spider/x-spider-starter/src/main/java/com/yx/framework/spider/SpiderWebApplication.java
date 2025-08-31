package com.yx.framework.spider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan("com.yx.framework.spider")
@SpringBootApplication
public class SpiderWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpiderWebApplication.class, args);
    }
}
