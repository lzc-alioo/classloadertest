package com.alioo.classloader.v2;


import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CustomClassLoaderTest2 {

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException,
            InstantiationException, NoSuchMethodException, InvocationTargetException {
        //自定义2个classloader
        ClassLoader myClassLoader1 = new ClassLoader() {
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

        ClassLoader myClassLoader2 = new ClassLoader() {
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

        //用类加载1加载Factory类
        Class factoryClass = myClassLoader1.loadClass(Factory.class.getName());

        Method createGoodsMethod = factoryClass.getMethod("createGoods");
        //反射调用createGoods方法，得到对象goods1
        Object goods1 = createGoodsMethod.invoke(factoryClass.newInstance());

        //用类加载2加载Trunk类
        Class trunkClass = myClassLoader2.loadClass(Trunk.class.getName());
        //用类加载2加载Goods类
        Class goodsClass = myClassLoader2.loadClass(Goods.class.getName());
        //用类加载2加载Goods类
        Object goods2 = goodsClass.newInstance();

        Method setGoodsMethod = trunkClass.getMethod("setGoods", goodsClass);
        //用类加载2得到的trunkClass、goods2，反射调用setGoods方法 执行结果：正常
        setGoodsMethod.invoke(trunkClass.newInstance(), goods2);

        //用类加载2得到的trunkClass、用类加载器1得到goods1 执行结果：报错 IllegalArgumentException: argument type mismatch
        setGoodsMethod.invoke(trunkClass.newInstance(), goods1);

    }
}


