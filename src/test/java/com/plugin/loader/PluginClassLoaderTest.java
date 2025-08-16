package com.plugin.loader;


import org.junit.jupiter.api.Test;
import java.net.URL;
import static org.junit.jupiter.api.Assertions.assertNull;

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
}
