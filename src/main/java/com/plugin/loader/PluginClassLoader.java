package com.plugin.loader;

import com.google.common.collect.Sets;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Objects;
import java.util.Set;

import static com.google.common.collect.Iterables.any;

/**
 * 自定义插件Class Loader,继承URLClassLoader
 */
@Slf4j
public class PluginClassLoader extends URLClassLoader {
    private final Set<String> excludedPackages;
    private final Set<String> overridePackages;

    public PluginClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
        this.excludedPackages = Sets.newHashSet("java.", "javax.", "jdk.", "javafx.", "sun.", "com.sun.", "oracle.");
        this.overridePackages = Sets.newHashSet();
    }

    public void addExcludedPackages(Set<String> excludedPackages) {
        this.excludedPackages.addAll(excludedPackages);
    }

    public void addOverridePackages(Set<String> overridePackages) {
        this.overridePackages.addAll(overridePackages);
    }

    private boolean isEligibleForOverriding(@NonNull String className) {
        return !isExcluded(className) && any(overridePackages, className::startsWith);
    }

    protected boolean isExcluded(@NonNull String className) {
        for (String packageName : this.excludedPackages) {
            if (className.startsWith(packageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 覆盖加载类
     *
     * @param className 类名
     * @return 类对象
     * @throws ClassNotFoundException 如果类未找到
     */
    private Class<?> loadClassForOverriding(String className) throws ClassNotFoundException {
        Class<?> result = findLoadedClass(className);
        if (Objects.isNull(result)) {
            try {
                // 尝试使用当前类加载器加载类
                result = findClass(className);
            } catch (ClassNotFoundException e) {
                // 如果当前类加载器找不到，尝试使用父类加载器加载
                if (log.isDebugEnabled()) {
                    log.debug("当前ClassLoader无法找到类, 尝试回退到父加载器加载: {}", className, e);
                }
                /**
                 * 只回退给父，不使用 super.loadClass
                 * super.loadClass 的默认流程是：父优先 → 若父找不到，再调用 当前 loader 的 findClass。
                 * 刚刚已经试过 findClass（抛了 ClassNotFoundException），再走一遍会重复尝试甚至再次抛异常。
                 */
                ClassLoader parentClassLoader = getParent();
                if (parentClassLoader != null) {
                    return parentClassLoader.loadClass(className);
                } else {
                    // 无父 → 走引导加载器
                    return Class.forName(className, false, null);
                }
            }
        }
        return result;
    }


    @Override
    protected Class<?> loadClass(String className, boolean resolve) throws ClassNotFoundException {
        Class<?> result;
        synchronized (getClassLoadingLock(className)) {
            if (isEligibleForOverriding(className)) {
                if (log.isInfoEnabled()) {
                    log.info("覆盖加载类: {}", className);
                }
                result = loadClassForOverriding(className);
                if (Objects.nonNull(result)) {
                    // 链接类
                    if (resolve) {
                        resolveClass(result);
                    }
                    return result;
                }
            }
            // 使用默认类加载方式
            return super.loadClass(className, resolve);
        }
    }
}
