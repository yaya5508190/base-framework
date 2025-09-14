package com.plugin.common;

import com.plugin.manager.Plugin;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
public class ModuleFederationConfig {
    public ModuleFederationConfig(Collection<Plugin> plugins, String contextPath) {
        plugins.forEach(plugin -> {
            Remote remote = new Remote();
            remote.setAlias(plugin.getPluginConfig().pluginId());
            remote.setName(plugin.getPluginConfig().pluginId());
            //如果contextPath是根路径，则不添加，否则添加contextPath
            String context = "/".equals(contextPath) ? "" : contextPath;
            remote.setEntry("." + context + "/" + plugin.getPluginConfig().pluginId() + "/mf-manifest.json");
            this.remotes.add(remote);

            // 添加菜单配置，支持一个插件多个菜单
            plugin.getPluginConfig().menus().forEach((menuName, componentName) -> {
                Menu menu = new Menu();
                menu.setName(menuName);
                menu.setPath("/" + plugin.getPluginConfig().pluginId() + "/" + componentName);
                menu.setComponent(plugin.getPluginConfig().pluginId() + "/" + componentName);
                this.menus.add(menu);
            });
        });
    }

    private List<Remote> remotes = new ArrayList<>();
    private List<Menu> menus = new ArrayList<>();

    @Data
    public static class Remote {
        private String alias;
        private String name;
        private String entry;
    }

    @Data
    public static class Menu {
        private String name;
        private String path;
        private String component;
    }
}
