package com.yx.framework.spider.model;

public record Result<T>(SpiderRequest request, T data) {}
