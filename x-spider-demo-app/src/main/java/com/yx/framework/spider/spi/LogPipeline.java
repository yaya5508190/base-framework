package com.yx.framework.spider.spi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LogPipeline implements Pipeline<String> {
    private static final Logger log = LoggerFactory.getLogger(LogPipeline.class);

    @Override
    public void process(String r) {
        log.info("TITLE={}", r);
    }
}
