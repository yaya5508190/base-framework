package com.yx.framework.spider.model;

import java.util.Map;

public record SpiderRequest(
        String url,
        Map<String, String> headers,
        String method,
        byte[] body
) {
    public static SpiderRequest get(String url) {
        return new SpiderRequest(url, Map.of(), "GET", null);
    }

    public static SpiderRequest get(String url, Map<String, String> headers) {
        return new SpiderRequest(url, headers, "GET", null);
    }

    public static SpiderRequest post(String url) {
        return new SpiderRequest(url, Map.of(), "POST", null);
    }

    public static SpiderRequest post(String url, Map<String, String> headers) {
        return new SpiderRequest(url, headers, "POST", null);
    }
}
