package com.yx.framework.spider.core;

import com.yx.framework.spider.config.SpiderProperties;
import com.yx.framework.spider.fetch.Fetcher;
import com.yx.framework.spider.model.Page;
import com.yx.framework.spider.model.Result;
import com.yx.framework.spider.model.SpiderRequest;
import com.yx.framework.spider.schedule.Scheduler;
import com.yx.framework.spider.spi.PageParser;
import com.yx.framework.spider.spi.Pipeline;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import java.net.URI;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class SpiderEngine {
    private final Scheduler scheduler;
    private final Fetcher fetcher;
    private final List<PageParser<?>> parsers;
    private final List<Pipeline<?>> pipelines;
    private final ExecutorService pool;
    private final SpiderProperties props;
    private final ConcurrentMap<String, RateLimiter> hostLimiters = new ConcurrentHashMap<>();


    public SpiderEngine(Scheduler scheduler, Fetcher fetcher, List<PageParser<?>> parsers,
                        List<Pipeline<?>> pipelines, ExecutorService pool, SpiderProperties props) {
        this.scheduler = scheduler;
        this.fetcher = fetcher;
        this.parsers = parsers;
        this.pipelines = pipelines;
        this.pool = pool;
        this.props = props;
    }


    public void submitSeeds(Collection<String> urls) {
        urls.stream().map(SpiderRequest::get).forEach(scheduler::push);
        for (int i = 0; i < props.getThreads(); i++) pool.submit(this::loop);
    }


    private void loop() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                SpiderRequest req = scheduler.poll(1, TimeUnit.SECONDS);
                if (req == null) continue;
                throttle(req.url());
                processOne(req);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception ignored) {
            }
        }
    }


    private void throttle(String url) {
        String host = URI.create(url).getHost();
        hostLimiters.computeIfAbsent(host, h -> RateLimiter.of(
                "host-" + h,
                RateLimiterConfig.custom()
                        .limitRefreshPeriod(Duration.ofSeconds(1))
                        .limitForPeriod(Math.max(1, (int) props.getRateLimit().getPerHostPermitsPerSecond()))
                        .timeoutDuration(Duration.ofSeconds(5))
                        .build()
        )).acquirePermission();
    }


    @Retryable(
            maxAttemptsExpression = "#{@SpiderProperties.retry.maxAttempts}",
            backoff = @Backoff(delayExpression = "#{@SpiderProperties.retry.backoffMs}")
    )
    void processOne(SpiderRequest req) throws Exception {
        Page page = fetcher.fetch(req);
        String contentType = page.contentType() == null ? "" : page.contentType().toLowerCase();
        for (PageParser<?> parser : parsers) {
            if ( parser.supports(req.url(), contentType)) {
                @SuppressWarnings("unchecked")
                List<Result<Object>> results = (List<Result<Object>>) (List<?>)  parser.parse(page);
                for (Result<Object> r : results) {
                    for (Pipeline<?> pipe : pipelines) {
                        @SuppressWarnings("unchecked")
                        Pipeline<Object> cast = (Pipeline<Object>) pipe;
                        cast.process(r);
                    }
                }
            }
        }
    }
}
