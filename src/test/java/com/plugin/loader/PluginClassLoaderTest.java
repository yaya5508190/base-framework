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
        // 创建一个插件目录和源文件
        Path pluginDir = Paths.get("src/test/java/com/plugin/loader/override");
        Path sourceDir = pluginDir.resolve(Paths.get("com", "override"));
        Files.createDirectories(sourceDir);
        Path sourceFile = sourceDir.resolve("Sample.java");
        String source = "package com.override; public class Sample { public String value() { return \"plugin\"; } }";
        Files.writeString(sourceFile, source);

        // 编译源文件
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        compiler.run(null, null, null, "-d", pluginDir.toString(), sourceFile.toString());
        Files.deleteIfExists(sourceFile);

        // 创建一个插件类加载器
        URL[] urls = {pluginDir.toUri().toURL()};
        PluginClassLoader classLoader = new PluginClassLoader(urls, ClassLoader.getSystemClassLoader());

        //不添加覆盖包的时候 应该使用父类加载器加载
        Class<?> clazz = classLoader.loadClass("com.override.Sample");
        Object instance = clazz.getDeclaredConstructor().newInstance();
        Method method = clazz.getDeclaredMethod("value");
        String result = (String) method.invoke(instance);
        assertEquals("parent", result);

        // 添加覆盖包后，应该使用PluginClassLoader加载
        classLoader.addOverridePackages(Set.of("com.override."));
        clazz = classLoader.loadClass("com.override.Sample");
        instance = clazz.getDeclaredConstructor().newInstance();
        method = clazz.getDeclaredMethod("value");
        result = (String) method.invoke(instance);
        assertEquals("plugin", result);
        classLoader.close();
    }
}
