package com.cc.demo.v3;


import com.lzc.alioo.container.plugin.x1.HelloOneModel;
import com.lzc.alioo.container.plugin.x1.HelloOneService;
import com.lzc.alioo.container.plugin.x2.HelloTwoModel;
import com.lzc.alioo.container.plugin.x2.HelloTwoService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BizTest3 {
    public static void main(String[] args) {
        try {
            {
                //测试是否可以获取到插件中的类HelloOneService 及 返回值是jdk中的类String（结论：可以）
                HelloOneService helloService11 = new HelloOneService();
                String result = helloService11.sayHello();
                log.info("call method:HelloOneService.sayHello return result: " + result);
            }
            {
                //测试是否可以获取到插件中的类HelloOneService 及 插件中的类HelloModel（结论：可以）
                HelloOneService helloService12 = new HelloOneService();
                HelloOneModel helloModel = helloService12.queryHelloModel("alioo");
                log.info("call method:HelloOneService.queryHelloModel return result: " + helloModel);
            }

            {
                //测试开辟的新线程是否可以获取到插件中的类HelloService 及 插件中的类HelloModel（结论：可以）
                //在一个线程A中创建的新线程B默认采用了线程A的类加载器
                Thread t1 = new Thread(() -> {
                    log.info("contextClassLoader:" + Thread.currentThread().getContextClassLoader());
                    HelloOneService helloService13 = new HelloOneService();
                    HelloOneModel helloModel = helloService13.queryHelloModel("开心时刻");
                    log.info("call method:HelloOneService.queryHelloModel return result: " + helloModel);
                }, "testNewThread");
                t1.start();
                t1.join();

            }

            {
                //测试是否可以获取到插件中的类HelloOneService 及 插件中的类HelloModel（结论：可以）
                HelloOneService helloService14 = new HelloOneService();
                Object helloModel = helloService14.queryObject("alioo14");
                log.info("call method:HelloOneService.queryObject return result: " + helloModel);
            }


//            {
//                //测试是否可以获取到插件中的类HelloService 及 插件中的类HelloModel（结论：可以）
//                HelloTwoService helloService21 = new HelloTwoService();
//                HelloTwoModel helloModel = helloService21.queryHelloModel("alioo21");
//                log.info("call method:HelloTwoService.queryHelloModel return result: " + helloModel);
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
