package com.plugin.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(PluginLoaderSelector.class)
public @interface EnablePluginLoader {
}
