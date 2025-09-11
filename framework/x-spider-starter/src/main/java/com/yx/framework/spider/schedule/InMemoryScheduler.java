package com.yx.framework.spider.schedule;

import com.yx.framework.spider.model.SpiderRequest;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class InMemoryScheduler implements Scheduler {
    private final BlockingQueue<SpiderRequest> q = new LinkedBlockingQueue<>();

    public void push(SpiderRequest req) {
        q.offer(req);
    }

    public SpiderRequest poll(long t, TimeUnit u) throws InterruptedException {
        return q.poll(t, u);
    }
}
