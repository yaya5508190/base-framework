package com.yx.framework.spider.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.yx.framework.spider.model.Page;
import com.yx.framework.spider.spi.HtmlBodyParser;
import com.yx.framework.spider.spi.JsonBodyParser;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HtmlParserEngine extends AbstractParserEngine<Page> {
    public HtmlParserEngine(List<HtmlBodyParser<?>> bodyParsers) {
        super(bodyParsers);
    }
}
