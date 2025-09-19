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
     * 当前插件含的菜单，一个插件可以注册多个菜单
     * @return Map<上级菜单名称/菜单名称, 组件名称>
     */
    Map<String,String> menus();
}
