package com.plugin.manager;

import api.PluginConfig;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.plugin.common.Constants;
import com.plugin.exception.PluginRuntimeException;
import com.plugin.loader.PluginClassLoader;
import com.plugin.utils.CommonUtils;
import com.plugin.utils.SpringUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.CachedIntrospectionResults;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

@Slf4j
@Component
public class PluginRegistrar {
    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Resource
    private PluginManager pluginManager;

    public Plugin register(Path jarPath) {
        if (!Files.exists(jarPath)) {
            throw new PluginRuntimeException("插件路径不存在: " + jarPath);
        }
        if (log.isInfoEnabled()) {
            log.info("开始注册插件: {}", jarPath);
        }
        PluginClassLoader pluginClassLoader;
        try {
            pluginClassLoader = new PluginClassLoader(jarPath.toUri().toURL(), applicationContext.getClassLoader());
        } catch (MalformedURLException e) {
            throw new PluginRuntimeException("pluginClassLoader创建失败", e);
        }

        List<PluginConfig> pluginConfigList = new ArrayList<>();
        ServiceLoader<PluginConfig> loadedDrivers = ServiceLoader.load(PluginConfig.class, pluginClassLoader);
        loadedDrivers.forEach(pluginConfigList::add);
        if (pluginConfigList.size() != 1) {
            throw new PluginRuntimeException("插件配置必须且只能有一个");
        }

        PluginConfig pluginConfig = pluginConfigList.get(0);
        checkNotNull(pluginConfig.name(), "pluginName不可为空");
        checkNotNull(pluginConfig.version(), "pluginVersion不可为空");
        checkNotNull(pluginConfig.pluginId(), "pluginId不可为空");

        //用于还原classloader
        ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();

        //设置当前线程的ClassLoader为插件ClassLoader
        Thread.currentThread().setContextClassLoader(pluginClassLoader);
        PluginApplicationContext pluginApplicationContext;
        List<Object> mvcControllers;
        List<RequestMappingInfo> resourceMappings;
        try {
            //获取并加载jar包中所有类
            Set<Class<?>> classes = CommonUtils.getClasses(pluginClassLoader, jarPath.toString());

            //初始化插件的ApplicationContext
            pluginApplicationContext = initPluginApplicationContext(pluginClassLoader, pluginConfig);

            //注册MVC Controller
            mvcControllers = registerMvcControllers(classes, pluginApplicationContext, pluginConfig);

            //注册静态资源处理器
            resourceMappings = registerStaticResources(pluginClassLoader,pluginApplicationContext);
        } catch (Exception e) {
            log.error("插件注册异常, pluginPath={}", jarPath, e);
            CachedIntrospectionResults.clearClassLoader(pluginClassLoader);
            throw new PluginRuntimeException("注册插件异常", e);
        } finally {
            //确保还原ClassLoader
            Thread.currentThread().setContextClassLoader(currentClassLoader);
        }
        //创建插件对象
        Plugin plugin = new Plugin(
                jarPath,
                pluginConfig,
                pluginApplicationContext,
                requestMappingHandlerMapping,
                resourceMappings,
                mvcControllers
        );
        return pluginManager.register(plugin);
    }

    private List<Object> registerMvcControllers(Set<Class<?>> classes, PluginApplicationContext pluginApplicationContext, PluginConfig pluginConfig) {
        List<Object> mvcControllers = Lists.newArrayList();
        for (Class<?> aClass : classes) {
            if (SpringUtils.isSpringController(aClass)) {
                //从上下文中获取Controller Bean
                Object controllerBean = pluginApplicationContext.getBean(aClass);
                //记录所有的ControllerBean
                mvcControllers.add(controllerBean);
                //注册Controller
                SpringUtils.handleControllerRegistration(
                        controllerBean,
                        requestMappingHandlerMapping,
                        pluginConfig.pluginId(),
                        Constants.REGISTERER);
            }
        }
        return mvcControllers;
    }

    private PluginApplicationContext initPluginApplicationContext(PluginClassLoader pluginClassLoader, PluginConfig pluginConfig) {
        //构建插件的ApplicationContext
        PluginApplicationContext pluginApplicationContext = new PluginApplicationContext();
        pluginApplicationContext.setParent(applicationContext);
        pluginApplicationContext.setClassLoader(pluginClassLoader);
        //扫描PluginConfig所在的包
        pluginApplicationContext.scan(Sets.newHashSet(pluginConfig.getClass().getPackage().getName()).toArray(new String[0]));
        pluginApplicationContext.refresh();
        return pluginApplicationContext;
    }

    private List<RequestMappingInfo>  registerStaticResources(PluginClassLoader pluginClassLoader,ApplicationContext puginApplicationContext) {
        List<RequestMappingInfo> resourceMapping = new ArrayList<>();
        ClassPathResource staticResource = new ClassPathResource("static/", pluginClassLoader);
        if (!staticResource.exists()) {
            return resourceMapping;
        }
        //注册静态资源处理器
        ResourceHttpRequestHandler handler = new ResourceHttpRequestHandler();
        handler.setLocations(java.util.Collections.singletonList(staticResource));
        handler.setApplicationContext(puginApplicationContext);
        try {
            handler.afterPropertiesSet();
        } catch (Exception e) {
            throw new PluginRuntimeException("初始化ResourceHttpRequestHandler失败", e);
        }

        RequestMappingInfo mappingInfo = RequestMappingInfo.paths("/**")
                .options(requestMappingHandlerMapping.getBuilderConfiguration())
                .build();
        try {
            Method method = ResourceHttpRequestHandler.class
                    .getMethod(
                            "handleRequest",
                            HttpServletRequest.class,
                            HttpServletResponse.class
                    );
            requestMappingHandlerMapping.registerMapping(mappingInfo, handler, method);
        } catch (NoSuchMethodException e) {
            throw new PluginRuntimeException("注册静态资源处理器失败", e);
        }
        resourceMapping.add(mappingInfo);
        return resourceMapping;
    }
}

