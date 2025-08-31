package com.yx.framework.spider.support;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


public class UserAgentProvider {
    private final List<String> uas;
    private final ThreadLocalRandom rnd = ThreadLocalRandom.current();

    public UserAgentProvider(List<String> uas) {
        this.uas = (uas == null || uas.isEmpty()) ? List.of("Mozilla/5.0") : uas;
    }

    public String pick() {
        return uas.get(rnd.nextInt(uas.size()));
    }
}
