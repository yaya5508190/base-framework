package com.plugin.utils;

import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Enumeration;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@Slf4j
public class CommonUtils {
    /**
     * 规范化前缀：确保以 "/" 开头，不以 "/" 结尾（根 "/" 除外）
     *
     * @param prefix 路径前缀
     * @return 规范化后的路径前缀
     */
    protected static String normalizePrefix(String prefix) {
        String p = prefix.trim();
        if (!p.startsWith("/")) {
            p = "/" + p;
        }
        // 保留根 "/"，其他结尾 "/" 去掉
        if (p.length() > 1 && p.endsWith("/")) {
            p = p.substring(0, p.length() - 1);
        }
        return p;
    }

    public static Set<Class<?>> getClasses(ClassLoader classLoader, String pluginPath) {
        Set<Class<?>> classes = Sets.newHashSet();
        try {
            File jar = new File(pluginPath);
            JarFile jarFile = new JarFile(jar);
            for (Enumeration<JarEntry> ea = jarFile.entries(); ea.hasMoreElements(); ) {
                JarEntry jarEntry = ea.nextElement();
                String name = jarEntry.getName();
                if (name.endsWith(".class")) {
                    String loadName = name.replace("/", ".").substring(0, name.length() - 6);
                    //加载class
                    Class<?> c = classLoader.loadClass(loadName);
                    classes.add(c);
                }
            }
            jarFile.close();
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("从{}读取所有class文件失败 ", pluginPath, e);
            }
        }

        return classes;
    }
}
