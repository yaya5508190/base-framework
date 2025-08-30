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
            remote.setEntry("." + contextPath + "/" + plugin.getPluginConfig().pluginId() + "/mf-manifest.json");
            this.remotes.add(remote);

            Menu menu = new Menu();
            menu.setName(plugin.getPluginConfig().name());
            menu.setPath("/" + plugin.getPluginConfig().pluginId());
            menu.setComponent(plugin.getPluginConfig().pluginId() + "/App");
            this.menus.add(menu);
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
