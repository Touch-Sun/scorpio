package com.touchsun.scorpio.core;

import cn.hutool.log.LogFactory;
import com.touchsun.scorpio.boot.CommonClassLoader;
import com.touchsun.scorpio.exception.ScorpioNormalException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Scorpio静态启动类
 *
 * @author Lee
 */
public class ScorpioApplication {

    /**
     * Scorpio App 启动方法
     *
     * @param serverClass 启动类
     * @param args           命令行参数
     */
    public static void run(Class<?> serverClass, String[] args) {
        try {
            // 准备公共类加载器
            CommonClassLoader commonClassLoader = new CommonClassLoader();
            // 设置当前线程的类加载器
            Thread.currentThread().setContextClassLoader(commonClassLoader);
            // 加载Server
            serverClass = commonClassLoader.loadClass(serverClass.getName());
            // 实例化Server
            Object server = serverClass.newInstance();
            // 执行start方法
            Method start = serverClass.getMethod("start");
            start.invoke(server);
        } catch (ScorpioNormalException e) {
            e.printStackTrace();
            LogFactory.get().error(e);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
