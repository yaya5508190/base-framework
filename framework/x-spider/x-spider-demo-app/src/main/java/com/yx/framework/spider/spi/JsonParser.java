package com.yx.framework.spider.spi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yx.framework.spider.enums.DataType;
import com.yx.framework.spider.model.Page;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class JsonParser implements PageParser<JsonNode> {
    @Override
    public boolean supports(String url, String ct) { return ct != null && ct.contains("application/json"); }

    @Override
    public ResultProperty getResultProperty() {
        return new ResultProperty("content", DataType.SINGLE);
    }

    @Override
    public List<JsonNode> parse(Page page) throws JsonProcessingException {
        Document doc = Jsoup.parse(new String(page.body(), StandardCharsets.UTF_8), page.request().url());
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(doc.body().text());
        return List.of(node);
    }
}
