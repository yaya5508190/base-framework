package com.plugin.common;

import com.plugin.manager.Plugin;
import lombok.Data;

import java.util.*;

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

            Map<String, Menu> parentMenuMap = new HashMap<>();
            List<Menu> subMenus = new ArrayList<>();
            // 添加菜单配置，支持一个插件多个菜单
            plugin.getPluginConfig().menus().forEach((menuName, componentName) -> {
                // 判断是否有上级菜单
                boolean isSubMenu = menuName.contains("/");

                if (isSubMenu) {
                    String parentMenuName = menuName.split("/")[0];
                    String childMenuName = menuName.split("/")[1];
                    Menu parentMenu = parentMenuMap.get(parentMenuName);
                    if (parentMenu == null) {
                        parentMenu = new Menu();
                        parentMenu.setName(parentMenuName);
                        parentMenu.setParent(true);
                        parentMenuMap.put(parentMenuName, parentMenu);
                        this.menus.add(parentMenu);
                    }
                    Menu childMenu = new Menu();
                    childMenu.setName(childMenuName);
                    childMenu.setPath("/" + plugin.getPluginConfig().pluginId() + "/" + componentName);
                    childMenu.setComponent(plugin.getPluginConfig().pluginId() + "/" + componentName);
                    parentMenu.getChildren().add(childMenu);
                } else {
                    Menu menu = new Menu();
                    menu.setName(menuName);
                    menu.setPath("/" + plugin.getPluginConfig().pluginId() + "/" + componentName);
                    menu.setComponent(plugin.getPluginConfig().pluginId() + "/" + componentName);
                    this.menus.add(menu);
                }
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
        private Boolean parent = false;
        private List<Menu> children = new ArrayList<>();
    }
}
