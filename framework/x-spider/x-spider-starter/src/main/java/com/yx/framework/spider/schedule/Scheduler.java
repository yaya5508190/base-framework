package com.yx.framework.spider.schedule;

import com.yx.framework.spider.model.SpiderRequest;

import java.util.concurrent.TimeUnit;

public interface Scheduler {
    void push(SpiderRequest req);
    SpiderRequest poll(long timeout, TimeUnit unit) throws InterruptedException;
}
