package com.yx.framework.spider.robot;

class RobotsTxt {
    static final RobotsTxt ALLOW_ALL = new RobotsTxt("");
    private final String raw;

    RobotsTxt(String raw) {
        this.raw = raw == null ? "" : raw;
    }

    boolean allowed(String url) {
        return true;
    } // minimal demo: always allow
}
