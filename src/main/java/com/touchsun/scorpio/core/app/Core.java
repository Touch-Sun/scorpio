package com.touchsun.scorpio.core.app;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.LogFactory;
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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Scorpio内核
 * @author Lee
 */
public class Core {

    /**
     * 构建服务器通讯
     * @return serverSocket
     * @throws IOException IO异常
     */
    public static ServerSocket buildConnect() throws IOException {
        Integer port = ScorpioConfig.DEFAULT_PORT;
        ServerSocket serverSocket = new ServerSocket(port);
        LogFactory.get().info(ScorpioConfig.MSG_SCORPIO_STARTED, port);
        return serverSocket;
    }

    /**
     * 处理请求
     * @param socket 客户端连接
     * @param host 虚拟主机
     * @throws IOException IO异常
     */
    public static void handleRequest(Socket socket, Host host) throws IOException {
        // 实例化Request对象解析Http,处理输入数据(请求)
        Request request = new Request(socket, host);
        requestLog(request);

        // 实例化Response对象,处理输出数据(响应)
        Response response = new Response();


        // 根据URI返回资源
        String uri = request.getUri();
        // 获取应用上下文,定位不同的Web应用
        Context appContext = request.getAppContext();

        if (ScorpioConfig.URI_ROOT.equals(uri)) {
            // "/"根路径返回欢迎内容
            response.getPrintWriter().println(ScorpioConfig.MSG_WELCOME);
        } else {
            // 去除"/"得到文件名称 [/hello.html -> hello.html]
            String fileName = StrUtil.removePrefix(uri, ScorpioConfig.URI_ROOT);
            // 根据文件名以及应用上下文去找文件
            File file = FileUtil.file(appContext.getAppPath(), fileName);
            if (file.exists()) {
                // 模拟线程阻塞页面[阻塞2s]
                if (StrUtil.equals(ScorpioConfig.PAGE_NAME_HTML_TIME_CONSUME, fileName)) {
                    ThreadUtil.sleep(2000);
                }
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

    /**
     * 响应200
     * @param socket 客户端连接
     * @param response 响应对象
     * @throws IOException IO异常
     */
    public static void reply200(Socket socket, Response response) throws IOException {
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
        infos.put(ScorpioConfig.JVM_JAVA_VENDOR_FIELD, ScorpioConfig.JVM_JAVA_VENDOR_VALUE);
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
