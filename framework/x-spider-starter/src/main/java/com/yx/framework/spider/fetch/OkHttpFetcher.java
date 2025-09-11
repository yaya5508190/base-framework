package com.yx.framework.spider.fetch;

import com.yx.framework.spider.model.Page;
import com.yx.framework.spider.model.SpiderRequest;
import com.yx.framework.spider.robot.RobotsService;
import com.yx.framework.spider.support.ProxyProvider;
import com.yx.framework.spider.support.UserAgentProvider;
import okhttp3.*;

import java.io.IOException;
import java.net.Proxy;

public class OkHttpFetcher implements Fetcher {
    private static final MediaType OCTET = MediaType.parse("application/octet-stream");
    private final OkHttpClient http;
    private final UserAgentProvider ua;
    private final ProxyProvider proxy;
    private final RobotsService robots;


    public OkHttpFetcher(OkHttpClient http, UserAgentProvider ua, ProxyProvider proxy, RobotsService robots) {
        this.http = http;
        this.ua = ua;
        this.proxy = proxy;
        this.robots = robots;
    }


    @Override
    public Page fetch(SpiderRequest req) throws IOException {
        if (!robots.allowed(req.url())) throw new IOException("Blocked by robots.txt: " + req.url());
        OkHttpClient client = http;
        Proxy p = proxy.pick();
        if (p != null) client = http.newBuilder().proxy(p).build();


        Request.Builder requestBuilder = new Request.Builder().url(req.url())
                .header("User-Agent", ua.pick())
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");


        for (var header : req.headers().entrySet()) {
            requestBuilder.header(header.getKey(), header.getValue());
        }

        // 修正：统一在 builder 上 build；POST 时显式 Content-Type
        if ("POST".equalsIgnoreCase(req.method()) && req.body() != null) {
            requestBuilder = requestBuilder.post(RequestBody.create(req.body(), OCTET));
        } else {
            requestBuilder = requestBuilder.get();
        }
        Request rq = requestBuilder.build();


        try (Response resp = client.newCall(rq).execute()) {
            ResponseBody body = resp.body();
            byte[] bytes = body != null ? body.bytes() : new byte[0];
            String ct = resp.header("Content-Type", "");
            return new Page(req, resp.code(), ct, bytes);
        }
    }
}
