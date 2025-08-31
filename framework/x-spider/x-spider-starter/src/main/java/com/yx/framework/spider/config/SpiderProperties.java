package com.yx.framework.spider.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "x-spider")
public class SpiderProperties {
    private boolean enabled = true;
    private int threads = 8;
    private int requestTimeoutMs = 15000;
    private Retry retry = new Retry();
    private RateLimit rateLimit = new RateLimit();
    private List<String> userAgents = List.of("Mozilla/5.0");
    private List<String> proxies = List.of();
    private Robots robots = new Robots();


    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public int getThreads() { return threads; }
    public void setThreads(int threads) { this.threads = threads; }
    public int getRequestTimeoutMs() { return requestTimeoutMs; }
    public void setRequestTimeoutMs(int requestTimeoutMs) { this.requestTimeoutMs = requestTimeoutMs; }
    public Retry getRetry() { return retry; }
    public void setRetry(Retry retry) { this.retry = retry; }
    public RateLimit getRateLimit() { return rateLimit; }
    public void setRateLimit(RateLimit rateLimit) { this.rateLimit = rateLimit; }
    public List<String> getUserAgents() { return userAgents; }
    public void setUserAgents(List<String> userAgents) { this.userAgents = userAgents; }
    public List<String> getProxies() { return proxies; }
    public void setProxies(List<String> proxies) { this.proxies = proxies; }
    public Robots getRobots() { return robots; }
    public void setRobots(Robots robots) { this.robots = robots; }


    public static class Retry {
        private int maxAttempts = 3;
        private long backoffMs = 800;
        public int getMaxAttempts() { return maxAttempts; }
        public void setMaxAttempts(int maxAttempts) { this.maxAttempts = maxAttempts; }
        public long getBackoffMs() { return backoffMs; }
        public void setBackoffMs(long backoffMs) { this.backoffMs = backoffMs; }
    }
    public static class RateLimit {
        private double perHostPermitsPerSecond = 1.0;
        public double getPerHostPermitsPerSecond() { return perHostPermitsPerSecond; }
        public void setPerHostPermitsPerSecond(double v) { this.perHostPermitsPerSecond = v; }
    }
    public static class Robots {
        private boolean respect = true;
        public boolean isRespect() { return respect; }
        public void setRespect(boolean respect) { this.respect = respect; }
    }
}
