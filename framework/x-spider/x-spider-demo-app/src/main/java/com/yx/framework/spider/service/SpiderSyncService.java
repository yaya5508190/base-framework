package com.yx.framework.spider.service;

import com.yx.framework.spider.core.ParserEngine;
import com.yx.framework.spider.enums.DataType;
import com.yx.framework.spider.fetch.Fetcher;
import com.yx.framework.spider.model.Page;
import com.yx.framework.spider.model.SpiderRequest;
import com.yx.framework.spider.spi.PageParser;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SpiderSyncService {

    private final Fetcher fetcher;
    private final ParserEngine parserEngine;

    public SpiderSyncService(Fetcher fetcher,  ParserEngine parserEngine) {
        this.fetcher = fetcher;
        this.parserEngine = parserEngine;
    }

    public Map<String, Object> getMethod(String url) throws Exception {
        Page page = fetcher.fetch(SpiderRequest.get(url));
        String context = Optional.ofNullable(page.contentType()).orElse("").toLowerCase();

        return parserEngine.parse(page, url, context);
    }

    public Map<String, Object> getMethod(String url, Map<String, String> headers) throws Exception {
        Page page = fetcher.fetch(SpiderRequest.get(url, headers));
        String context = Optional.ofNullable(page.contentType()).orElse("").toLowerCase();

        return parserEngine.parse(page, url, context);
    }
}
