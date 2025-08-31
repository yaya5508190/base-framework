package com.yx.framework.spider.spi;

import com.yx.framework.spider.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LogPipeline implements Pipeline<String> {
    private static final Logger log = LoggerFactory.getLogger(LogPipeline.class);

    @Override
    public void process(Result<String> r) {
        log.info("URL={} TITLE={}", r.request().url(), r.data());
    }
}
