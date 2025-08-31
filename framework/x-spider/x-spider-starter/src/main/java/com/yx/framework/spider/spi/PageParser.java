package com.yx.framework.spider.spi;

import com.yx.framework.spider.model.Page;
import com.yx.framework.spider.model.Result;

import java.util.List;

public interface PageParser<T> {
    boolean supports(String url, String contentType);

    List<Result<T>> parse(Page page) throws Exception;
}
