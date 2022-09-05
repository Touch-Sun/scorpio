package com.touchsun.scorpio.boot;

import cn.hutool.log.LogFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * 公共类加载器
 *
 * @author lee
 */
public class CommonClassLoader extends URLClassLoader {

    public CommonClassLoader() {
        super(new URL[] {});

        // 获取工作目录
        File workerFolder = new File(System.getProperty("user.dir"));
        // 获取Lib目录
        File libFolder = new File(workerFolder, "lib");
        // 获取Lib目录下所有文件
        File[] libFiles = libFolder.listFiles();
        if (libFiles != null) {
            // 将所有的Jar绝对路径加入到URL列表中
            for (File file : libFiles) {
                if (file.getName().endsWith("jar")) {
                    try {
                        URL url = new URL("file:" + file.getAbsolutePath());
                        super.addURL(url);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        LogFactory.get().error("加载Lib目录下的Jar包异常, URL: {}", file.getAbsolutePath());
                    }
                }
            }
        }
    }
}
