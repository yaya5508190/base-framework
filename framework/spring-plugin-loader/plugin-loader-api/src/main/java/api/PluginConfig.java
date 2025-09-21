package api;

import java.util.List;
import java.util.Map;

/**
 * 插件配置接口
 */
public interface PluginConfig {
    String name();

    String version();

    String desc();

    String pluginId();

    /**
     * 当前插件包含的组件
     * @return 组件列表
     */
    List<PluginComponent> pluginComponents();
}
