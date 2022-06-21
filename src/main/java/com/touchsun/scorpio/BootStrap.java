package com.touchsun.scorpio;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.NetUtil;
import cn.hutool.core.util.StrUtil;
import com.touchsun.scorpio.core.config.ScorpioConfig;
import com.touchsun.scorpio.core.constant.ResponseConstant;
import com.touchsun.scorpio.core.web.Request;
import com.touchsun.scorpio.core.web.Response;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

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

                // 实例化Request对象解析Http,处理输入数据(请求)
                Request request = new Request(socket);
                System.out.println(("收到Http请求信息：" + request.getRequestContent()));
                System.out.println(("请求Uri：" + request.getUri()));

                // 实例化Response对象,处理输出数据(响应)
                Response response = new Response();
                String html = "Hi, Welcome to Scorpio!";
                response.getPrintWriter().println(html);

                reply200(socket,response);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 响应200
     * @param socket 客户端连接
     * @param response 响应对象
     * @throws IOException IO异常
     */
    private static void reply200(Socket socket, Response response) throws IOException {
        // 构建头部信息
        String header = StrUtil.format(ResponseConstant.RESPONSE_200, ResponseConstant.TEXT_HTML);
        // 解析头部/身体的字节信息
        byte[] headerBytes = header.getBytes();
        byte[] bodyBytes = response.getBody();

        // 构建整体字节信息
        byte[] responseBytes = new byte[headerBytes.length + bodyBytes.length];
        // 头部/身体的字节信息拷贝到整体字节信息中 [...headerBytes.....bodyBytes....]
        ArrayUtil.copy(headerBytes, 0, responseBytes, 0, headerBytes.length);
        ArrayUtil.copy(bodyBytes, 0, responseBytes, headerBytes.length, bodyBytes.length);

        // 响应数据
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(responseBytes);
        outputStream.flush();
        socket.close();
    }
}
















