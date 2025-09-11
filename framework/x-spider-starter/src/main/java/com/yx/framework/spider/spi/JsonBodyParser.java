package com.yx.framework.spider.spi;

import com.fasterxml.jackson.databind.JsonNode;

public interface JsonBodyParser<T> extends BodyParser<T, JsonNode> {
}
