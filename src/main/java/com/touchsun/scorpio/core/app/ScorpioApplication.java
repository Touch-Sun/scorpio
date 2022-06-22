package com.touchsun.scorpio.core.app;

import cn.hutool.log.LogFactory;
import com.touchsun.scorpio.core.plugin.ThreadHelper;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Scorpio App
 * @author Lee
 */
public class ScorpioApplication {

    /**
     * Scorpio App 启动方法
     * @param bootStrapClass 启动类
     * @param args 命令行参数
     */
    public static void run(Class<?> bootStrapClass, String[] args) {
        try {
            // 启动Scorpio日志
            Core.jvmLog();

            // 开启服务器套接字通讯
            ServerSocket serverSocket = Core.buildConnect();

            // 循环：处理一个socket连接后在处理下一个
            while (true) {
                // 收到的浏览器的一个请求
                Socket socket = serverSocket.accept();
                // 向线程池提交处理请求任务
                ThreadHelper.run(() -> {
                    try {
                        Core.handleRequest(socket);
                    } catch (IOException e) {
                        e.printStackTrace();
                        LogFactory.get().error(e);
                    }
                });
            }

        } catch (IOException e) {
            e.printStackTrace();
            LogFactory.get().error(e);
        }
    }
}
