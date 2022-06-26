package com.touchsun.scorpio.core.app;

import cn.hutool.log.LogFactory;
import com.touchsun.scorpio.core.plugin.ThreadActuator;
import com.touchsun.scorpio.exception.ScorpioNormalException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Scorpio服务器
 * @author Lee
 */
public class Server {

    /**
     * Scorpio服务
     */
    private Service service;

    public Server() throws ScorpioNormalException {
        this.service = Service.instance(this);
    }

    public static Server instance() throws ScorpioNormalException {
        return new Server();
    }

    /**
     * 启动Scorpio服务
     */
    public void start() {
        Core.jvmLog();
        this.initialize();
    }

    /**
     * 初始化Scorpio服务
     */
    public void initialize() {
        try {
            // 开启服务器套接字通讯
            ServerSocket serverSocket = Core.buildConnect();

            // 循环：处理一个socket连接后在处理下一个
            while (true) {
                // 收到的浏览器的一个请求
                Socket socket = serverSocket.accept();
                // 向线程池提交处理请求任务
                ThreadActuator.run(() -> {
                    try {
                        Core.handleRequest(socket, service);
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
