package com.demo;

import api.PluginConfig;
import com.google.auto.service.AutoService;

import java.util.Map;

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
        return "943a6b1b-222a-4a99-b900-3744271480e6";
    }

    @Override
    public String parentMenuName() {
        return "";
    }

    @Override
    public Map<String, String> menus() {
        return Map.of("测试插件-菜单","App","测试插件-菜单1","App1");
    }
}
