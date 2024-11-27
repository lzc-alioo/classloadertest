package com.cc.demo.v4;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;


public class CustomClassLoaderTest4 {

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException,
            InstantiationException {
        String str = "123";
        //自定义一个classloader
//        ClassLoader myClassLoader = new URLClassLoader(new URL[]{});

        ClassLoader myClassLoader = new ClassLoader() {
            @Override
            public Class<?> loadClass(String name) {
                try {
                    String fileName = name.substring(name.lastIndexOf(".") + 1) + ".class";
                    InputStream is = getClass().getResourceAsStream(fileName);
                    if (is == null) {
                        Class clazz = super.loadClass(name);
                        System.out.println("myClassLoader loaded name:" + name + " by super classloader:" + clazz.getClassLoader());
                        return clazz;
                    }
                    byte[] b = new byte[is.available()];
                    is.read(b);
                    return defineClass(name, b, 0, b.length);

                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        Class myClass = myClassLoader.loadClass(String.class.getName());
        System.out.println("myClassLoader:" + myClassLoader);
        //此处结果为 myClass:class com.alioo.classloader.v1.CustomClassLoaderTest
        System.out.println("myClass:" + myClass);

        ClassLoader defaultClassLoader = String.class.getClassLoader();
        //此处结果为 defaultClassLoader:jdk.internal.loader.ClassLoaders$AppClassLoader@512ddf17
        System.out.println("defaultClassLoader:" + defaultClassLoader);

        Object myInstance = myClass.newInstance();
        //此处值为：false
        System.out.println(myInstance instanceof String);

        //此处会报java.lang.ClassCastException（原因，虽然是同一个类，但是被不同类加载器加载也不行）
        String a = (String) myInstance;

    }
}