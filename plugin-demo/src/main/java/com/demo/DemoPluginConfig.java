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
    public String pluginId() {
        return "943a6b1b-222a-4e99-b900-3744270480e6";
    }
}
