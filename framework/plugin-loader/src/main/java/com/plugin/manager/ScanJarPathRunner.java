package com.plugin.manager;

import com.google.common.base.Strings;
import com.plugin.exception.PluginRuntimeException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author chenhaiming
 */
@Slf4j
@Component
public class ScanJarPathRunner implements CommandLineRunner, EnvironmentAware {


    private String pluginPath;

    private Boolean scan = Boolean.FALSE;

    @Resource
    PluginRegistrar pluginRegistrar;

    @Override
    public void run(String... args) {
        if (!Strings.isNullOrEmpty(pluginPath) && scan) {
            if (log.isInfoEnabled()) {
                log.info("开始扫描插件路径: {}", pluginPath);
            }
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(pluginPath), "*.jar")) {
                stream.forEach(pluginRegistrar::register);
            } catch (Exception e) {
                throw new PluginRuntimeException("加载插件异常", e);
            }
        }
    }


    @Override
    public void setEnvironment(Environment environment) {
        pluginPath = environment.getProperty("plugin.path");
        String value = environment.getProperty("plugin.scan");
        scan = Strings.isNullOrEmpty(value) ? Boolean.FALSE : Boolean.valueOf(value);
    }
}
