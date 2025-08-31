package com.yx.framework.spider.model;

public record Page(SpiderRequest request, int status, String contentType, byte[] body) {}
