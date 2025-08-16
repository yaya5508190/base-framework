package com.plugin.loader;


import org.junit.jupiter.api.Test;
import java.net.URL;
import java.lang.reflect.Method;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PluginClassLoaderTest {
    @Test
    public void TestLoaderClass() {
        System.out.println(1);
    }

    @Test
    public void shouldNotOverrideExcludedPackages() throws ClassNotFoundException {
        PluginClassLoader classLoader = new PluginClassLoader(new URL[0], ClassLoader.getSystemClassLoader());
        Class<?> clazz = classLoader.loadClass("java.lang.String");
        assertNull(clazz.getClassLoader());
    }

    @Test
    public void shouldRespectAddedExcludedPackages() throws Exception {
        PluginClassLoader classLoader = new PluginClassLoader(new URL[0], ClassLoader.getSystemClassLoader());
        classLoader.addExcludedPackages(Set.of("com.example."));
        Method method = PluginClassLoader.class.getDeclaredMethod("isExcluded", String.class);
        method.setAccessible(true);
        boolean excluded = (boolean) method.invoke(classLoader, "com.example.Foo");
        assertTrue(excluded);
    }
}
