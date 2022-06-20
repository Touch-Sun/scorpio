package com.touchsun.scorpio;

import cn.hutool.core.util.NetUtil;
import com.touchsun.scorpio.core.config.ScorpioConfig;
import com.touchsun.scorpio.core.web.Request;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * 启动类
 * @author Lee
 */
public class BootStrap {

    public static void main(String[] args) {
        run();
    }

    private static void run() {
        try {
            Integer port = ScorpioConfig.DEFAULT_PORT;

            // 检测端口占用情况
            if (!NetUtil.isUsableLocalPort(port)) {
                System.out.println("端口[{" + port + "}]已经被占用，启动失败，请检测后重新启动……");
                return;
            }

            System.out.println("Scorpio启动成功,监听端口[io-" + port + "]");

            // 开启服务器套接字通讯
            ServerSocket serverSocket = new ServerSocket(port);

            // 循环：处理一个socket连接后在处理下一个
            while (true) {
                // 收到的浏览器的一个请求
                Socket socket = serverSocket.accept();

                // 实例化Request对象解析Http
                Request request = new Request(socket);
                System.out.println(("收到Http请求信息：" + request.getRequestContent()));
                System.out.println(("请求Uri：" + request.getUri()));


                // 处理输出数据(响应)
                OutputStream outputStream = socket.getOutputStream();
                String responseHead = "HTTP/1.1 200 OK\r\n" + "Content-Type: text/html\r\n\r\n";
                String responseContent = "Hi, Welcome to Scorpio!";
                String response = responseHead + responseContent;
                outputStream.write(response.getBytes());
                outputStream.flush();
                socket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
















