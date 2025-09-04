package com.yx.framework.spider.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.yx.framework.spider.spi.JsonBodyParser;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JsonParserEngine extends AbstractParserEngine<JsonNode> {
    public JsonParserEngine(List<JsonBodyParser<?>> bodyParsers) {
        super(bodyParsers);
    }
}
