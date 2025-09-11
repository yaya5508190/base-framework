package com.yx.framework.spider.fetch;

import com.yx.framework.spider.model.Page;
import com.yx.framework.spider.model.SpiderRequest;

import java.io.IOException;

public interface Fetcher {
    Page fetch(SpiderRequest request) throws IOException;
}
