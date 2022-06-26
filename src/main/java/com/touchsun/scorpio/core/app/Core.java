package com.touchsun.scorpio.core.app;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.LogFactory;
import com.touchsun.scorpio.core.config.ScorpioConfig;
import com.touchsun.scorpio.core.constant.ResponseConstant;
import com.touchsun.scorpio.core.constant.ResponseStatus;
import com.touchsun.scorpio.core.web.Request;
import com.touchsun.scorpio.core.web.Response;
import com.touchsun.scorpio.exception.ExceptionMessage;
import com.touchsun.scorpio.exception.ScorpioNormalException;

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

    public static Core instance() {
        return new Core();
    }

    /**
     * 构建服务器通讯
     * @return serverSocket
     * @throws IOException IO异常
     */
    public ServerSocket buildConnect() throws IOException {
        Integer port = ScorpioConfig.DEFAULT_PORT;
        ServerSocket serverSocket = new ServerSocket(port);
        LogFactory.get().info(ScorpioConfig.MSG_SCORPIO_STARTED, port);
        return serverSocket;
    }

    /**
     * 处理请求
     * @param socket 客户端连接
     * @param service Scorpio服务
     * @throws IOException IO异常
     */
    public void handleRequest(Socket socket, Service service) throws IOException, ScorpioNormalException {
        // 实例化Request对象解析Http,处理输入数据(请求)
        Request request = new Request(socket, service);
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
                this.reply(socket, response, ResponseStatus._200);
            } else {
                // 写入[文件未找到信息]
                response.getPrintWriter().println(ResponseConstant.RESPONSE_404_HTML);
                this.reply(socket, response, ResponseStatus._404);
            }
        }

        // 响应日志
        this.responseLog(response);
    }

    /**
     * 响应
     * @param socket 客户端连接
     * @param responseStatus 响应状态
     * @param response 响应对象
     * @throws IOException IO异常
     */
    public void reply(Socket socket, Response response, ResponseStatus responseStatus) throws IOException, ScorpioNormalException {
        // 根据响应类型构建响应头
        String header;
        switch (responseStatus) {
            case _200:
                header = StrUtil.format(ResponseConstant.RESPONSE_200_HEADER, ResponseConstant.TEXT_HTML);
            break;
            case _404:
                header = ResponseConstant.RESPONSE_404_HEADER;
                break;
            default:
                throw new ScorpioNormalException(ExceptionMessage.RESPONSE_STATUS_NOT_EXIST_EXCEPTION);
        }
        this.reply(socket, response, header);
    }

    /**
     * 响应
     * @param socket 客户端连接
     * @param header 响应头
     * @param response 响应对象
     * @throws IOException IO异常
     */
    private void reply(Socket socket, Response response, String header) throws IOException {
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
    public void jvmLog() {
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
    public void requestLog(Request request) {
        LogFactory.get().info("Scorpio收到请求：\n" + request.getRequestContent());
    }

    /**
     * Response组件响应信息
     * @param response response 组件
     */
    public void responseLog(Response response) {
        LogFactory.get().info("Scorpio进行应答: " + new String(response.getBody(), StandardCharsets.UTF_8));
    }
}
