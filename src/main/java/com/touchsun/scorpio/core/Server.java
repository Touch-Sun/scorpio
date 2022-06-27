package com.touchsun.scorpio.core;

import cn.hutool.log.LogFactory;
import com.touchsun.scorpio.plugin.ThreadActuator;
import com.touchsun.scorpio.exception.ScorpioNormalException;
import com.touchsun.scorpio.web.Request;
import com.touchsun.scorpio.web.Response;

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

    /**
     * Scorpio内核
     */
    private Core core;

    public Server(Core core) throws ScorpioNormalException {
        this.service = Service.instance(this);
        this.core = core;
    }

    public static Server instance(Core core) throws ScorpioNormalException {
        return new Server(core);
    }

    /**
     * 启动Scorpio服务
     */
    public void start() {
        this.core.jvmLog();
        this.initialize();
    }

    /**
     * 初始化Scorpio服务
     */
    public void initialize() {
        try {
            // 开启服务器套接字通讯
            ServerSocket serverSocket = this.core.buildConnect();

            // 循环：处理一个socket连接后在处理下一个
            while (true) {
                // 收到的浏览器的一个请求
                Socket socket = serverSocket.accept();
                // 实例化Request对象解析Http,处理输入数据(请求)
                Request request = new Request(socket, this.service);
                // 实例化Response对象,处理输出数据(响应)
                Response response = new Response();
                // 向线程池提交处理请求任务
                ThreadActuator.run(() -> {
                    try {
                        // 处理请求
                        this.core.handleRequest(socket, request, response);
                    } catch (IOException | ScorpioNormalException e) {
                        e.printStackTrace();
                        LogFactory.get().error(e);
                        try {
                            // 发生异常响应异常
                            this.core.reply(socket, response, e);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                            LogFactory.get().error(e);
                        }
                    }
                });
            }

        } catch (IOException e) {
            e.printStackTrace();
            LogFactory.get().error(e);
        }
    }
}
