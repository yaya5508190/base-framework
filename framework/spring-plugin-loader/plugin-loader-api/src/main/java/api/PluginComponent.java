package api;

/**
 * 插件组件,一个插件可以包含多个组件，每个组件的类型可以是菜单，可以是某些其他功能，例如搜索弹框等等
 */
public record PluginComponent(
        String pluginId, //所属插件ID
        String type, //组件类型，可以是菜单组件，可以是某些其他功能组件，例如搜索弹框等等 具体由业务定义
        String meta, //组件元数据，例如菜单组件的元数据可以是 上级菜单/菜单名称"
        String componentName, //微前端组件名称
        String desc
) {
    public String getComponent() {
        return pluginId + "/" + componentName;
    }
}
