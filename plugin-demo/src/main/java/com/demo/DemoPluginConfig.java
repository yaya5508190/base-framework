package com.demo;

import api.PluginConfig;
import com.google.auto.service.AutoService;

@AutoService(PluginConfig.class)
public class DemoPluginConfig implements PluginConfig {
    @Override
    public String name() {
        return "测试插件";
    }

    @Override
    public String version() {
        return "0.0.1";
    }

    @Override
    public String desc() {
        return "demo plugin 插件";
    }

    @Override
    public String pathPrefix() {
        return "";
    }

}
