package com.yx.framework.spider.spi;

import com.fasterxml.jackson.databind.JsonNode;
import com.yx.framework.spider.enums.DataType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JsonParser implements JsonBodyParser<JsonNode> {
    @Override
    public boolean supports(String url, String ct) { return ct != null && ct.contains("application/json"); }

    @Override
    public ResultProperty getResultProperty() {
        return new ResultProperty("content", DataType.SINGLE);
    }

    @Override
    public List<JsonNode> parse(JsonNode node){
        return List.of(node);
    }
}
