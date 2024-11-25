package com.alioo.classloader.v3;


import lombok.extern.slf4j.Slf4j;
import org.example1.HelloOneModel;
import org.example1.HelloOneService;

@Slf4j
public class BizTest3 {
    public static void main(String[] args) {
        try {
            {
                //测试是否可以获取到插件中的类HelloOneService 及 返回值是jdk中的类String（结论：可以）
                HelloOneService helloService = new HelloOneService();
                String result = helloService.sayHello();
                log.info("call method:sayHello return result: " + result);
            }
            {
                //测试是否可以获取到插件中的类HelloService 及 插件中的类HelloModel（结论：可以）
                HelloOneService helloService2 = new HelloOneService();
                HelloOneModel helloModel = helloService2.queryHelloModel("alioo");
                log.info("call method:queryHelloModel return result: " + helloModel);
            }

            {
                //测试开辟的新线程是否可以获取到插件中的类HelloService 及 插件中的类HelloModel（结论：可以）
                //在一个线程A中创建的新线程B默认采用了线程A的类加载器
                new Thread(() -> {
                    log.info("contextClassLoader:" + Thread.currentThread().getContextClassLoader());
                    HelloOneService helloService2 = new HelloOneService();
                    HelloOneModel helloModel = helloService2.queryHelloModel("开心时刻");
                    log.info("call method:queryHelloModel return result: " + helloModel);
                }, "testNewThread").start();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
