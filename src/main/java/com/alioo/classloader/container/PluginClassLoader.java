package com.alioo.classloader.container;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Slf4j
public class PluginClassLoader extends URLClassLoader {


    private static PluginClassLoader instance;
    private static boolean inited;

    private String classPath;

    private PluginClassLoader(String module, String classPath, URL[] urlPath) {
        super(module, urlPath, ClassLoader.getSystemClassLoader().getParent());

        this.classPath = classPath;
        PluginSharableClassLoader.register(this);
    }


    public static PluginClassLoader init(String module, String classPath) {
        if (inited) {
            return instance;
        }

        URL[] urlPath = new URL[0];
        try {
            urlPath = new URL[]{new File(classPath).toURI().toURL()};
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        instance = new PluginClassLoader(module, classPath, urlPath);
        inited = true;
        return instance;

    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        Class clazz = null;
        //对于import的类优先使用lzcClassLoader进行统一加载

        clazz = loadClassData(name);
        if (clazz != null) {
            log.info("Loaded By " + this.getName() + "(" + this + ") name: " + name);
        }

        return clazz;

    }

    public Class<?> loadClassData(String className) {
        Class clazz = findLoadedClass(className);
        if (clazz != null) {
            return clazz;
        }
        try {
            return super.loadClass(className);
        } catch (ClassNotFoundException e) {
        }


        return null;
    }


    public List<String> getExportedClass() {
        //todo:实际情况可能是只需要导出部分类文件
        List<String> list = new ArrayList<>();
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(classPath))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().endsWith(".class")) {
                    list.add(entry.getName());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

}

