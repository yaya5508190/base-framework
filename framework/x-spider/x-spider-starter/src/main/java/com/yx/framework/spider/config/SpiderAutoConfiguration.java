package com.yx.framework.spider.config;

import com.yx.framework.spider.core.SpiderEngine;
import com.yx.framework.spider.fetch.Fetcher;
import com.yx.framework.spider.fetch.OkHttpFetcher;
import com.yx.framework.spider.robot.RobotsService;
import com.yx.framework.spider.schedule.InMemoryScheduler;
import com.yx.framework.spider.schedule.Scheduler;
import com.yx.framework.spider.spi.PageParser;
import com.yx.framework.spider.spi.Pipeline;
import com.yx.framework.spider.support.ProxyProvider;
import com.yx.framework.spider.support.UserAgentProvider;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.*;

@Configuration
@EnableRetry
public class SpiderAutoConfiguration {
    @Bean
    public OkHttpClient okHttpClient(SpiderProperties props) {
        return new OkHttpClient.Builder()
                .callTimeout(Duration.ofMillis(props.getRequestTimeoutMs()))
                .followRedirects(true)
                .build();
    }

    @Bean(destroyMethod="shutdown")
    public ExecutorService spiderExecutor(SpiderProperties props) {
        return new ThreadPoolExecutor(
                props.getThreads(), props.getThreads(),
                60, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(props.getThreads() * 100),
                r -> {
                    Thread t = new Thread(r);
                    t.setName("x-spider-" + t.getId());
                    t.setDaemon(true);
                    return t;
                },
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    @Bean
    public UserAgentProvider userAgentProvider(SpiderProperties props) {
        return new UserAgentProvider(props.getUserAgents());
    }

    @Bean
    public ProxyProvider proxyProvider(SpiderProperties props) {
        return new ProxyProvider(props.getProxies());
    }

    @Bean
    public RobotsService robotsService(SpiderProperties props, OkHttpClient http) {
        return new RobotsService(http, props.getRobots().isRespect());
    }

    @Bean
    public Fetcher fetcher(OkHttpClient http, UserAgentProvider ua, ProxyProvider proxy, RobotsService robots) {
        return new OkHttpFetcher(http, ua, proxy, robots);
    }

    @Bean
    public Scheduler scheduler() {
        return new InMemoryScheduler();
    }

    @Bean
    public SpiderEngine spiderEngine(
            Scheduler scheduler, Fetcher fetcher, List<PageParser<?>> parsers,
            List<Pipeline<?>> pipelines, ExecutorService pool, SpiderProperties props) {
        return new SpiderEngine(scheduler, fetcher, parsers, pipelines, pool, props);
    }
}
