package com.yx.framework.spider.controller;

import com.yx.framework.spider.service.SpiderSyncService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/spider")
public class SpiderController {
    private final SpiderSyncService spiderSyncService;

    public SpiderController(SpiderSyncService spiderSyncService) {
        this.spiderSyncService = spiderSyncService;
    }

    @GetMapping("/title")
    public ResponseEntity<Map<String, Object>> title(@RequestParam String url) throws Exception {
        return ResponseEntity.ok(spiderSyncService.getMethod(url));
    }

    @GetMapping("/json")
    public ResponseEntity<Map<String, Object>> json(@RequestParam String url) throws Exception {
        Map<String, String> headers = Map.ofEntries(
                Map.entry("accept", "application/json, text/plain, */*"),
                Map.entry("accept-language", "zh-CN,zh;q=0.9"),
                Map.entry("cache-control", "no-cache"),
                Map.entry("origin", "https://movie.douban.com"),
                Map.entry("pragma", "no-cache"),
                Map.entry("priority", "u=1, i"),
                Map.entry("referer", "https://movie.douban.com/explore?support_type=movie&is_all=false&category=%E7%83%AD%E9%97%A8&type=%E5%85%A8%E9%83%A8"),
                Map.entry("sec-ch-ua", "\"Not;A=Brand\";v=\"99\", \"Google Chrome\";v=\"139\", \"Chromium\";v=\"139\""),
                Map.entry("sec-ch-ua-mobile", "?0"),
                Map.entry("sec-ch-ua-platform", "\"Windows\""),
                Map.entry("sec-fetch-dest", "empty"),
                Map.entry("sec-fetch-mode", "cors"),
                Map.entry("sec-fetch-site", "same-site"),
                Map.entry("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/139.0.0.0 Safari/537.36")
        );
        return ResponseEntity.ok(spiderSyncService.getMethod(url,headers));
    }
}
