package com.plugin.manager;

import api.PluginConfig;
import com.plugin.common.Constants;
import com.plugin.utils.SpringUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.CachedIntrospectionResults;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.beans.Introspector;
import java.io.IOException;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.List;
import java.util.ResourceBundle;

import static com.google.common.base.Preconditions.checkNotNull;

@Data
@Slf4j
public class Plugin {
    //插件的jar包路径
    private Path pluginPath;
    //插件配置
    private PluginConfig pluginConfig;
    //插件的spring应用上下文
    private PluginApplicationContext pluginApplicationContext;
    //全局RequestMappingHandlerMapping
    private RequestMappingHandlerMapping requestMappingHandlerMapping;
    //插件的资源处理器
    private List<RequestMappingInfo> resourceHandlersMappings;
    //插件的Controller
    private List<Object> mvcControllers;

    public Plugin(
            Path pluginPath,
            PluginConfig pluginConfig,
            PluginApplicationContext pluginApplicationContext,
            RequestMappingHandlerMapping requestMappingHandlerMapping,
            List<RequestMappingInfo> resourceHandlersMappings,
            List<Object> mvcControllers) {
        this.pluginPath = pluginPath;
        this.pluginConfig = pluginConfig;
        this.pluginApplicationContext = pluginApplicationContext;
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
        this.resourceHandlersMappings = resourceHandlersMappings;
        this.mvcControllers = mvcControllers;
    }

    /**
     * 卸载插件
     *
     * @throws Exception 异常
     */
    public void unRegister() throws Exception {
        if (log.isInfoEnabled()) {
            log.info("卸载插件: name={}, version={}", pluginConfig.name(), pluginConfig.version());
        }
        // 取消注册Controller
        unRegisterController(requestMappingHandlerMapping);
        // 取消注册资源处理器
        unRegisterResourceHandlers(requestMappingHandlerMapping);
        // 关闭 spring context
        closeApplicationContext(pluginApplicationContext);
        // 关闭 class loader
        clearClassLoader(pluginApplicationContext.getClassLoader());
        // 删除插件
        // Files.deleteIfExists(jarPath);
    }

    private void closeApplicationContext(ConfigurableApplicationContext applicationContext) {
        checkNotNull(applicationContext, "ApplicationContext为空");
        applicationContext.close();
    }

    private void clearClassLoader(ClassLoader classLoader) throws IOException {
        checkNotNull(classLoader, "ClassLoader为空");
        // Introspector缓存BeanInfo类来获得更好的性能。卸载时刷新所有Introspector的内部缓存。
        Introspector.flushCaches();
        // 从已经使用给定类加载器加载的缓存中移除所有资源包
        ResourceBundle.clearCache(classLoader);
        // clear the introspection cache for the given ClassLoader
        CachedIntrospectionResults.clearClassLoader(classLoader);
        // close
        if (classLoader instanceof URLClassLoader) {
            ((URLClassLoader) classLoader).close();
        }
    }

    private void unRegisterController(RequestMappingHandlerMapping mapping) {
        checkNotNull(mapping, "RequestMappingHandlerMapping 为空");
        for (Object controllerBean : mvcControllers) {
            SpringUtils.handleControllerRegistration(controllerBean, mapping, pluginConfig.pluginId(), Constants.UN_REGISTERER);
        }
    }

    private void unRegisterResourceHandlers(RequestMappingHandlerMapping mapping) {
        if (resourceHandlersMappings == null || resourceHandlersMappings.isEmpty()) {
            return;
        }
        for (RequestMappingInfo requestMappingInfo : resourceHandlersMappings) {
            mapping.unregisterMapping(requestMappingInfo);
        }
    }
}
