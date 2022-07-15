package com.touchsun.scorpio.core;

import cn.hutool.log.LogFactory;
import com.touchsun.scorpio.config.ScorpioConfig;
import com.touchsun.scorpio.exception.ScorpioNormalException;
import com.touchsun.scorpio.plugin.ThreadActuator;
import com.touchsun.scorpio.web.Request;
import com.touchsun.scorpio.web.Response;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Scorpio连接器
 *
 * @author Lee
 */
public class Connector implements Runnable {

    /**
     * 连接器端口
     */
    @Setter
    private int port;

    /**
     * Scorpio服务
     */
    @Getter
    private Service service;

    /**
     * Scorpio内核
     */
    private Core core;

    public Connector(Service service) {
        this.service = service;
        this.core = service.getCore();
    }

    public static Connector instance(Service service) {
        return new Connector(service);
    }

    /**
     * 初始化
     */
    public void initialize() {
        LogFactory.get().debug(ScorpioConfig.MSG_SCORPIO_INITIALIZE, port);
    }

    /**
     * 线程准备就绪
     */
    public void start() {
        // 提交到线程执行器
        ThreadActuator.executeScorpioInstance(this);
        LogFactory.get().info(ScorpioConfig.MSG_SCORPIO_STARTED, port);
    }

    @Override
    public void run() {
        try {
            // 开启服务器套接字通讯
            ServerSocket serverSocket = this.core.buildConnect(this.port);

            // 循环：处理一个socket连接后在处理下一个
            while (true) {
                // 收到的浏览器的一个请求
                Socket socket = serverSocket.accept();
                // 实例化Request对象解析Http,处理输入数据(请求)
                Request request = new Request(socket, this.service);
                // 实例化Response对象,处理输出数据(响应)
                Response response = new Response();
                // 向线程池提交处理请求任务
                ThreadActuator.runWebTask(() -> {
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
                            LogFactory.get().error(ex);
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
