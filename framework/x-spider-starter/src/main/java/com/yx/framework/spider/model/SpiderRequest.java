package com.yx.framework.spider.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.FormBody;
import okio.Buffer;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
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

    public static SpiderRequest post(String url, Map<String, String> headers, Object jsonBody) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        byte[] body = mapper.writeValueAsString(jsonBody).getBytes(StandardCharsets.UTF_8);

        return new SpiderRequest(url, headers, "POST", body);
    }

    public static SpiderRequest post(String url, Map<String, String> headers, Map<String,String> fromBody) throws Exception {
        FormBody.Builder formBuilder = new FormBody.Builder();
        fromBody.forEach(formBuilder::add);
        okhttp3.RequestBody formBody = formBuilder.build();
        // 获取字节数组
        Buffer buffer = new Buffer();
        formBody.writeTo(buffer);
        byte[] body = buffer.readByteArray();
        return new SpiderRequest(url, headers, "POST", body);
    }
}
