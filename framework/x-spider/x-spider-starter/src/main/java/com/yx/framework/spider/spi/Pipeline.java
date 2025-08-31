package com.yx.framework.spider.spi;

import com.yx.framework.spider.model.Result;

public interface Pipeline<T> {
    void process(Result<T> result) throws Exception;
}
