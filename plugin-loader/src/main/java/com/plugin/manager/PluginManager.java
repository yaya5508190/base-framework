package com.plugin.manager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.base.Preconditions.checkNotNull;

@Slf4j
@Component
public class PluginManager implements DisposableBean {
    // 已注册的所有插件
    private final ConcurrentHashMap<String, Plugin> allPlugins = new ConcurrentHashMap<>();

    public Plugin register(Plugin plugin) {
        checkNotNull(plugin, "插件对象不能为空");
        String pluginName = plugin.getPluginConfig().name();
        String pluginVersion = plugin.getPluginConfig().version();
        checkNotNull(pluginName, "插件名称不能为空");
        checkNotNull(pluginVersion, "插件版本不能为空");
        return allPlugins.put(genPluginKey(pluginName, pluginVersion), plugin);
    }

    public void unRegister(String pluginName, String pluginVersion) throws Exception {
        this.find(pluginName, pluginVersion).unRegister();
        this.remove(pluginName, pluginVersion);
    }

    public Plugin find(String pluginName, String pluginVersion) {
        checkNotNull(pluginName, "插件名称不能为空");
        checkNotNull(pluginVersion, "插件版本不能为空");
        return allPlugins.get(genPluginKey(pluginName, pluginVersion));
    }

    private void remove(String pluginName, String pluginVersion) {
        checkNotNull(pluginName, "插件名称不能为空");
        checkNotNull(pluginVersion, "插件版本不能为空");
        allPlugins.remove(genPluginKey(pluginName, pluginVersion));
    }

    @Override
    public void destroy() throws Exception {
        allPlugins.forEach((name, plugin) -> {
            try {
                plugin.unRegister();
            } catch (Exception e) {
                if (log.isErrorEnabled()) {
                    log.error("插件卸载失败: {}", name, e);
                }
            }
        });
    }

    public Collection<Plugin> getAllPlugins() {
        return allPlugins.values();
    }

    private String genPluginKey(String pluginName, String pluginVersion) {
        return pluginName + "@" + pluginVersion;
    }
}
