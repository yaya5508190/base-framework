package com.yx.framework.spider.robot;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


import java.net.URI;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RobotsService {
    private final OkHttpClient http;
    private final boolean respect;
    private final ConcurrentMap<String, RobotsTxt> cache = new ConcurrentHashMap<>();

    public RobotsService(OkHttpClient http, boolean respect) {
        this.http = http;
        this.respect = respect;
    }

    public boolean allowed(String url) {
        if (!respect) return true;
        String host = URI.create(url).getHost();
        RobotsTxt r = cache.computeIfAbsent(host, this::fetch);
        return r.allowed(url);
    }

    private RobotsTxt fetch(String host) {
        try {
            Request req = new Request.Builder().url("https://" + host + "/robots.txt").build();
            try (Response resp = http.newCall(req).execute()) {
                String body = resp.isSuccessful() && resp.body() != null ? resp.body().string() : "";
                return new RobotsTxt(body);
            }
        } catch (Exception e) {
            return RobotsTxt.ALLOW_ALL;
        }
    }
}
