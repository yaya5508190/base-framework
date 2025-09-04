package com.yx.framework.spider.core;

import com.yx.framework.spider.enums.DataType;
import com.yx.framework.spider.spi.BodyParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractParserEngine<T> {
    private final List<? extends BodyParser<?, T>> parsers;

    protected AbstractParserEngine(List<? extends BodyParser<?, T>>parsers) {
        this.parsers = parsers;
    }

    public Map<String, Object> parse(T body, String url, String context) throws Exception {
        Map<String, Object> parsed = new HashMap<>();
        for (BodyParser<?,T> parser : parsers) {
            if (!parser.supports(url, context)) continue;
            @SuppressWarnings("unchecked")
            List<Object> results = (List<Object>) parser.parse(body);
            BodyParser.ResultProperty resultProperty = parser.getResultProperty();
            parsed.put(resultProperty.propertyName(),
                    resultProperty.dataType().equals(DataType.SINGLE)
                            ? results.get(0)
                            : results
            );
        }
        return parsed;
    }
}
