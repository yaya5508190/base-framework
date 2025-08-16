package com.plugin.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(PluginLoadBeanSelector.class)
public @interface EnablePluginLoadServer {
}
