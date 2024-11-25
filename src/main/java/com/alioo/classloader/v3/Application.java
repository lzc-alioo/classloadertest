package com.alioo.classloader.v3;

import com.alioo.classloader.container.AliooApplication;

public class Application {
    public static void main(String[] args) {

        try {
            //为了使用自定义类加载器将插件相关代码加载进来，这里将相关代码放到了AliooApplication进行处理
            AliooApplication.run(args);

            BizTest3.main(args);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
