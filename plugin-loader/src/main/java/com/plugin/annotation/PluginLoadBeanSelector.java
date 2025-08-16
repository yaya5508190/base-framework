package com.plugin.annotation;

import com.google.common.collect.Lists;
import com.plugin.manager.PluginManager;
import com.plugin.manager.PluginRegistrar;
import com.plugin.manager.ScanJarPathRunner;
import io.micrometer.common.lang.NonNullApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.List;
import java.util.Map;

/**
 * @author chenhaiming
 */
@Slf4j
@NonNullApi
public class PluginLoadBeanSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        Map<String, Object> attrs = importingClassMetadata.getAnnotationAttributes(EnablePluginLoadServer.class.getName());
        this.initEnableConfig(attrs);

        List<String> selectors = Lists.newArrayList();
        selectors.add(PluginRegistrar.class.getName());
        selectors.add(PluginManager.class.getName());
        selectors.add(ScanJarPathRunner.class.getName());
//        selectors.add(PluginAutoConfigurationRegistrar.class.getName());

        return selectors.toArray(new String[]{});
    }

    private void initEnableConfig(Map<String, Object> attrs){
    }
}
