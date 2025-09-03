package com.yx.framework.spider.spi;

import com.yx.framework.spider.enums.DataType;
import com.yx.framework.spider.model.Page;

import java.util.List;

public interface PageParser<T> {
    boolean supports(String url, String contentType);

    //返回结果的配置
    default ResultProperty getResultProperty(){
        return new ResultProperty("data", DataType.COLLECTION);
    }

    List<T> parse(Page page) throws Exception;

    /**
     * 定义返回的属性名称
     * 返回值类型是多个还是单个
     */
    record ResultProperty(String propertyName, DataType dataType){};
}
