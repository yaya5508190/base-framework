package com.yx.framework.spider.core;

import com.yx.framework.spider.enums.DataType;
import com.yx.framework.spider.model.Page;
import com.yx.framework.spider.spi.PageParser;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class ParserEngine {
    private final List<PageParser<?>> parsers;

    public ParserEngine(List<PageParser<?>> parsers) {
        this.parsers = parsers;
    }

    public Map<String, Object> parse(Page page, String url, String context) throws Exception {
        Map<String, Object> parsed = new HashMap<>();
        for (PageParser<?> parser : parsers) {
            if (!parser.supports(url, context)) continue;
            @SuppressWarnings("unchecked")
            List<Object> results = (List<Object>) parser.parse(page);
            PageParser.ResultProperty resultProperty = parser.getResultProperty();
            parsed.put(resultProperty.propertyName(),
                    resultProperty.dataType().equals(DataType.SINGLE)
                            ? results.get(0)
                            : results
            );
        }

        Map<String, Object> resp = new LinkedHashMap<>();
        resp.put("requestUrl", url);
        resp.put("status", page.status());
        resp.put("contentType", context);
        resp.put("parsed", parsed);
        return resp;
    }
}
