package com.yx.framework.spider.spi;

public interface Pipeline<T> {
    void process(T result) throws Exception;
}
