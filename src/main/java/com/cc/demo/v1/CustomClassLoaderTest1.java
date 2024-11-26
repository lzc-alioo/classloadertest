package com.cc.demo.v1;

import java.io.IOException;
import java.io.InputStream;


public class CustomClassLoaderTest1 {

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException,
            InstantiationException {
        //自定义一个classloader
        ClassLoader myClassLoader = new ClassLoader() {
            @Override
            public Class<?> loadClass(String name) {
                try {
                    String fileName = name.substring(name.lastIndexOf(".") + 1) + ".class";
                    InputStream is = getClass().getResourceAsStream(fileName);
                    if (is == null) {
                        return super.loadClass(name);
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

        Class myClass = myClassLoader.loadClass(CustomClassLoaderTest1.class.getName());
        System.out.println("myClassLoader:" + myClassLoader);
        //此处结果为 myClass:class com.alioo.classloader.v1.CustomClassLoaderTest
        System.out.println("myClass:" + myClass);

        ClassLoader systemClassLoader = CustomClassLoaderTest1.class.getClassLoader();
        //此处结果为 systemClassLoader:jdk.internal.loader.ClassLoaders$AppClassLoader@512ddf17
        System.out.println("systemClassLoader:" + systemClassLoader);

        Object myInstance = myClass.newInstance();
        //此处值为：false
        System.out.println(myInstance instanceof CustomClassLoaderTest1);

        //此处会报java.lang.ClassCastException（原因，虽然是同一个类，但是被不同类加载器加载也不行）
        CustomClassLoaderTest1 a = (CustomClassLoaderTest1) myInstance;

    }
}