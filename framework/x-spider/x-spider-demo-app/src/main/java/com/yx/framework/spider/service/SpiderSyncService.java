package com.yx.framework.spider.service;

import com.yx.framework.spider.fetch.Fetcher;
import com.yx.framework.spider.model.Page;
import com.yx.framework.spider.model.Result;
import com.yx.framework.spider.model.SpiderRequest;
import com.yx.framework.spider.spi.PageParser;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class SpiderSyncService {

    private final Fetcher fetcher;
    private final List<PageParser<?>> parsers;

    public SpiderSyncService(Fetcher fetcher, List<PageParser<?>> parsers) {
        this.fetcher = fetcher;
        this.parsers = parsers;
    }

    public Map<String, Object> getMethod(String url) throws Exception {
        Page page = fetcher.fetch(SpiderRequest.get(url));
        String context = Optional.ofNullable(page.contentType()).orElse("").toLowerCase();

        return parse(page, url, context);
    }

    public Map<String, Object> getMethod(String url,Map<String, String> headers) throws Exception {
        Page page = fetcher.fetch(SpiderRequest.get(url,headers));
        String context = Optional.ofNullable(page.contentType()).orElse("").toLowerCase();

        return parse(page, url, context);
    }

    private Map<String, Object> parse(Page page, String url, String context) throws Exception {
        List<Map<String, Object>> parsed = new ArrayList<>();
        for (PageParser<?> parser : parsers) {
            if (!parser.supports(url, context)) continue;
            @SuppressWarnings("unchecked")
            List<Result<Object>> results = (List<Result<Object>>) (List<?>) parser.parse(page);
            for (Result<Object> r : results) {
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("url", r.request().url());
                item.put("data", r.data()); // TitleParser 会是 String
                parsed.add(item);
            }
        }

        Map<String, Object> resp = new LinkedHashMap<>();
        resp.put("requestUrl", url);
        resp.put("status", page.status());
        resp.put("contentType", context);
        // 可选：返回部分原文（调试用）
        resp.put("snippet", new String(page.body(), StandardCharsets.UTF_8)
                .replaceAll("\\s+", " ")
                .substring(0, Math.min(200, page.body().length)));
        resp.put("parsed", parsed);
        return resp;
    }
}
