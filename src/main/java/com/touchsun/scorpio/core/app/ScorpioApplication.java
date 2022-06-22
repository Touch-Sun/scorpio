package com.touchsun.scorpio.core.app;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.LogFactory;
import com.touchsun.scorpio.BootStrap;
import com.touchsun.scorpio.core.config.ScorpioConfig;
import com.touchsun.scorpio.core.constant.ResponseConstant;
import com.touchsun.scorpio.core.web.Request;
import com.touchsun.scorpio.core.web.Response;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

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
    public static void run(Class<BootStrap> bootStrapClass, String[] args) {
        try {
            // 启动Scorpio日志
            jvmLog();

            // 开启服务器套接字通讯
            Integer port = ScorpioConfig.DEFAULT_PORT;
            ServerSocket serverSocket = new ServerSocket(port);
            LogFactory.get().info("Scorpio启动成功,监听端口[io-" + port + "]");

            // 循环：处理一个socket连接后在处理下一个
            while (true) {
                // 收到的浏览器的一个请求
                Socket socket = serverSocket.accept();

                // 实例化Request对象解析Http,处理输入数据(请求)
                Request request = new Request(socket);
                requestLog(request);

                // 实例化Response对象,处理输出数据(响应)
                Response response = new Response();

                // 根据URI返回资源
                String uri = request.getUri();
                if (ScorpioConfig.URI_ROOT.equals(uri)) {
                    // "/"根路径返回欢迎内容
                    response.getPrintWriter().println(ScorpioConfig.MSG_WELCOME);
                } else {
                    // 去除"/"得到文件名称 [/hello.html -> hello.html]
                    String fileName = StrUtil.removePrefix(uri, ScorpioConfig.URI_ROOT);
                    // 根据文件名找文件
                    File file = FileUtil.file(ScorpioConfig.ROOT_FOLDER, fileName);
                    if (file.exists()) {
                        // 读取文件内容
                        String fileContent = FileUtil.readUtf8String(file);
                        // 写入响应对象
                        response.getPrintWriter().println(fileContent);
                    } else {
                        // 写入[文件未找到信息]
                        response.getPrintWriter().println(ScorpioConfig.MSG_FILE_NOT_FOUND);
                    }
                }

                // 响应流
                reply200(socket,response);
                responseLog(response);
            }

        } catch (IOException e) {
            e.printStackTrace();
            LogFactory.get().error(e);
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

    /**
     * Scorpio启动日志
     */
    public static void jvmLog() {
        Map<String, String> infos = new LinkedHashMap<>(15);
        infos.put(ScorpioConfig.JVM_SERVER_VERSION_FILED, ScorpioConfig.JVM_SERVER_VERSION_VALUE);
        infos.put(ScorpioConfig.JVM_SERVER_BUILD_TIME_FIELD, ScorpioConfig.JVM_SERVER_BUILD_TIME_VALUE);
        infos.put(ScorpioConfig.JVM_OS_NAME_FIELD, ScorpioConfig.JVM_OS_NAME_VALUE);
        infos.put(ScorpioConfig.JVM_OS_VERSION_FIELD, ScorpioConfig.JVM_OS_VERSION_VALUE);
        infos.put(ScorpioConfig.JVM_OS_ARCHITECTURE_FIELD, ScorpioConfig.JVM_OS_ARCHITECTURE_VALUE);
        infos.put(ScorpioConfig.JVM_JAVA_HOME_FIELD, ScorpioConfig.JVM_JAVA_HOME_VALUE);
        infos.put(ScorpioConfig.JVM_JAVA_VERSION_FIELD, ScorpioConfig.JVM_JAVA_VERSION_VALUE);
        infos.put(ScorpioConfig.JVM_JAVA_VENDOR_FIELD, ScorpioConfig.JVM_JAVA_VERSION_VALUE);
        Set<String> keySet = infos.keySet();
        for (String key : keySet) {
            LogFactory.get().debug(key + ScorpioConfig.SYMBOL_COLON
                    + ScorpioConfig.SYMBOL_TAB_TAB
                    + infos.get(key));
        }
    }

    /**
     * Request组件请求信息
     * @param request request 组件
     */
    public static void requestLog(Request request) {
        LogFactory.get().info("Scorpio收到请求：\n" + request.getRequestContent());
    }

    /**
     * Response组件响应信息
     * @param response response 组件
     */
    public static void responseLog(Response response) {
        LogFactory.get().info("Scorpio进行应答: " + new String(response.getBody(), StandardCharsets.UTF_8));
    }
}
