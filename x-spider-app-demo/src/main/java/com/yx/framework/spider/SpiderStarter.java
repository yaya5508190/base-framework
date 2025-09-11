package com.yx.framework.spider;

import com.yx.framework.spider.core.SpiderEngine;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SpiderStarter implements ApplicationRunner {
    private final SpiderEngine engine;
    public SpiderStarter(SpiderEngine engine) { this.engine = engine; }


    @Override
    public void run(ApplicationArguments args) {
        engine.submitSeeds(List.of(
                "https://news.ycombinator.com/",
                "https://spring.io/",
                "https://www.wikipedia.org/"
        ));
    }
}
