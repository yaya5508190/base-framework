package com.yx.framework.spider.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yx.framework.spider.core.AbstractParserEngine;
import com.yx.framework.spider.core.HtmlParserEngine;
import com.yx.framework.spider.core.JsonParserEngine;
import com.yx.framework.spider.fetch.Fetcher;
import com.yx.framework.spider.model.Page;
import com.yx.framework.spider.model.SpiderRequest;
import com.yx.framework.spider.spi.HtmlBodyParser;
import com.yx.framework.spider.spi.JsonBodyParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class SpiderSyncService {

    private final Fetcher fetcher;
    private final JsonParserEngine jsonParserEngine;
    private final HtmlParserEngine htmlParserEngine;

    public SpiderSyncService(Fetcher fetcher,
                             JsonParserEngine jsonParserEngine,
                             HtmlParserEngine htmlParserEngine) {
        this.fetcher = fetcher;
        this.jsonParserEngine = jsonParserEngine;
        this.htmlParserEngine = htmlParserEngine;
    }

    public Map<String, Object> getMethod(String url) throws Exception {
        Page page = fetcher.fetch(SpiderRequest.get(url));
        String context = Optional.ofNullable(page.contentType()).orElse("").toLowerCase();

        return htmlParserEngine.parse(page, url, context);
    }

    public Map<String, Object> getMethod(String url, Map<String, String> headers) throws Exception {
        Page page = fetcher.fetch(SpiderRequest.get(url, headers));
        String context = Optional.ofNullable(page.contentType()).orElse("").toLowerCase();
        Document doc = Jsoup.parse(new String(page.body(), StandardCharsets.UTF_8), page.request().url());
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(doc.body().text());
        return jsonParserEngine.parse(node, url, context);
    }
}
