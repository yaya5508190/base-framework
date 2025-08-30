package com.plugin.annotation;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ComponentScan(basePackages = "com.plugin")
@Import(PluginLoaderSelector.class)
public @interface EnablePluginLoader {
}
