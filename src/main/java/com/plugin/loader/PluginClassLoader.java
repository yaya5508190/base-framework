package com.plugin.loader;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * 自定义插件Class Loader,继承URLClassLoader
 */
public class PluginClassLoader extends URLClassLoader {
    public PluginClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }
}
