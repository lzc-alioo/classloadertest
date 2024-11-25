package com.alioo.classloader.container;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;


public class PluginSharableClassLoader extends URLClassLoader {
    private static final Map<String, PluginClassLoader> pluginCache = new HashMap<>();


    public PluginSharableClassLoader() {
        super(new URL[0], ClassLoader.getSystemClassLoader().getParent());
    }

    public static void register(PluginClassLoader pluginClassLoader) {
        //pluginClassLoader提取出所有url中jar中class文件
        pluginClassLoader.getExportedClass().stream().forEach(name -> pluginCache.put(name, pluginClassLoader));

    }

    public Class<?> loadClass(String name) throws ClassNotFoundException {
        Class clazz = null;
        //预判一下当前加载的类是否在某个插件类加载器中

        PluginClassLoader pluginClassLoader = pluginCache.get(name.replace('.', '/') + ".class");
        if (pluginClassLoader != null) {
            clazz = pluginClassLoader.loadClassData(name);
            return clazz;
        }
        return null;


    }
}
