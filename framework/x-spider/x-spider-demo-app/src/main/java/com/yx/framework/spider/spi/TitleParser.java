package com.yx.framework.spider.spi;
import com.yx.framework.spider.model.Page;
import com.yx.framework.spider.model.Result;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;


import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;


@Component
public class TitleParser implements PageParser<String> {
    @Override
    public boolean supports(String url, String ct) { return ct != null && ct.contains("text/html"); }


    @Override
    public List<Result<String>> parse(Page page) {
        Document doc = Jsoup.parse(new String(page.body(), StandardCharsets.UTF_8), page.request().url());
        String title = Optional.of(doc.title()).orElse("");
        return List.of(new Result<>(page.request(), title));
    }
}
