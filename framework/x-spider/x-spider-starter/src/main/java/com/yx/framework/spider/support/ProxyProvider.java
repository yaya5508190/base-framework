package com.yx.framework.spider.support;

import java.net.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class ProxyProvider {
    private final List<Proxy> proxies;
    private final AtomicInteger idx = new AtomicInteger();

    public ProxyProvider(List<String> urls) {
        this.proxies = (urls == null) ? List.of() : urls.stream().map(ProxyProvider::toProxy).toList();
    }

    public Proxy pick() {
        if (proxies.isEmpty()) return null;
        return proxies.get(Math.floorMod(idx.getAndIncrement(), proxies.size()));
    }

    private static Proxy toProxy(String u) {
        URI uri = URI.create(u);
        return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(uri.getHost(), uri.getPort()));
    }
}
