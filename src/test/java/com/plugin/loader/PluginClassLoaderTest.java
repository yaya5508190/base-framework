package com.plugin.loader;


import com.override.Sample;
import org.junit.jupiter.api.Test;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class PluginClassLoaderTest {
    /**
     * java标准的类是由系统类加载器加载的，所以classLoader为null
     * 这里测试的是PluginClassLoader是否会覆盖java.lang.String类的加载
     * @throws Exception
     */
    @Test
    public void shouldNotOverrideExcludedPackages() throws Exception {
        PluginClassLoader classLoader = new PluginClassLoader(new URL[0], ClassLoader.getSystemClassLoader());
        Class<?> clazz = classLoader.loadClass("java.lang.String");
        classLoader.close();
        assertNull(clazz.getClassLoader());
    }

    @Test
    public void shouldRespectAddedExcludedPackages() throws Exception {
        PluginClassLoader classLoader = new PluginClassLoader(new URL[0], ClassLoader.getSystemClassLoader());
        classLoader.addExcludedPackages(Set.of("com.example."));
        boolean excluded = classLoader.isExcluded("com.example.Foo");
        classLoader.close();
        assertTrue(excluded);
    }

    @Test
    public void shouldLoadPluginClassWhenOverriding() throws Exception {
        Path pluginDir = Paths.get("src/test/java/com/plugin/loader/override");
        Path sourceDir = pluginDir.resolve(Paths.get("com", "override"));
        Files.createDirectories(sourceDir);
        Path sourceFile = sourceDir.resolve("Sample.java");
        String source = "package com.override; public class Sample { public String value() { return \"plugin\"; } }";
        Files.writeString(sourceFile, source);

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        compiler.run(null, null, null, "-d", pluginDir.toString(), sourceFile.toString());
        Files.deleteIfExists(sourceFile);

        URL[] urls = {pluginDir.toUri().toURL()};
        PluginClassLoader classLoader = new PluginClassLoader(urls, ClassLoader.getSystemClassLoader());

        Class<?> clazz = classLoader.loadClass("com.override.Sample");
        Object instance = clazz.getDeclaredConstructor().newInstance();
        Method method = clazz.getDeclaredMethod("value");
        String result = (String) method.invoke(instance);
        assertEquals("parent", result);

        // 添加覆盖包
        classLoader.addOverridePackages(Set.of("com.override."));
        clazz = classLoader.loadClass("com.override.Sample");
        instance = clazz.getDeclaredConstructor().newInstance();
        method = clazz.getDeclaredMethod("value");
        result = (String) method.invoke(instance);
        assertEquals("plugin", result);
        classLoader.close();
    }
}
