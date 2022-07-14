package com.touchsun.scorpio.core;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.LogFactory;
import com.touchsun.scorpio.config.ScorpioConfig;
import com.touchsun.scorpio.constant.ResponseConstant;
import com.touchsun.scorpio.exception.ExceptionMessage;
import com.touchsun.scorpio.exception.ScorpioNormalException;
import com.touchsun.scorpio.plugin.AppXMLParser;
import com.touchsun.scorpio.servlet.HelloServlet;
import com.touchsun.scorpio.type.ResponseStatus;
import com.touchsun.scorpio.web.Request;
import com.touchsun.scorpio.web.Response;

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
 *
 * @author Lee
 */
public class Core {

    public static Core instance() {
        return new Core();
    }

    /**
     * 构建服务器通讯
     *
     * @return serverSocket
     * @throws IOException IO异常
     */
    public ServerSocket buildConnect(int port) throws IOException {
        return new ServerSocket(port);
    }

    /**
     * 处理请求
     *
     * @param socket 客户端连接
     * @throws IOException IO异常
     */
    public void handleRequest(Socket socket, Request request, Response response)
            throws IOException, ScorpioNormalException {
        // 请求日志
        requestLog(request);

        // 根据URI返回资源
        String uri = request.getUri();
        // 获取应用上下文,定位不同的Web应用
        Context appContext = request.getAppContext();

        // 模拟Servlet响应
        if (StrUtil.equals(ScorpioConfig.URI_SERVLET, uri)) {
            HelloServlet helloServlet = HelloServlet.instance();
            helloServlet.doGet(request, response);
        } else {
            if (ScorpioConfig.URI_ROOT.equals(uri)) {
                switch (ScorpioConfig.DEFAULT_WELCOME_TYPE_STRATEGY) {
                    case TEXT:
                        // "/"根路径返回欢迎内容[文本信息]
                        textProcess(socket, response);
                        break;
                    case HTML:
                        // "/"根路径返回欢迎内容[HTML文件信息]
                        uri = AppXMLParser.parseWelcomeFile(request.getAppContext());
                        fileProcess(socket, response, uri, appContext);
                        break;
                    default:
                        throw new ScorpioNormalException(ExceptionMessage.WELCOME_STRATEGY_EXCEPTION);
                }
            } else {
                // 解析文件内容做出响应
                fileProcess(socket, response, uri, appContext);
            }
        }

        // 响应日志
        this.responseLog(response);
    }

    /**
     * 读取配置文字信息,做出响应
     *
     * @param socket   客户端连接
     * @param response 响应对象
     * @throws ScorpioNormalException 常规异常
     * @throws IOException            IO异常
     */
    private void textProcess(Socket socket, Response response) throws IOException, ScorpioNormalException {
        response.getWriter().println(ScorpioConfig.MSG_WELCOME);
        this.reply(socket, response, ResponseStatus._200);
    }

    /**
     * 解析文件内容,做出响应
     *
     * @param socket     客户端连接
     * @param response   响应对象
     * @param uri        URI
     * @param appContext 应用上下文
     * @throws ScorpioNormalException 常规异常
     * @throws IOException            IO异常
     */
    public void fileProcess(Socket socket, Response response, String uri, Context appContext)
            throws ScorpioNormalException, IOException {
        // 去除"/"得到文件名称 [/hello.html -> hello.html]
        String fileName = StrUtil.removePrefix(uri, ScorpioConfig.URI_ROOT);
        // 根据文件名以及应用上下文去找文件
        File file = FileUtil.file(appContext.getAppPath(), fileName);
        if (file.exists()) {
            // 模拟线程阻塞页面[阻塞2s]
            if (StrUtil.equals(ScorpioConfig.PAGE_NAME_HTML_TIME_CONSUME, fileName)) {
                ThreadUtil.sleep(2000);
            }
            // 模拟异常页面[主动抛出异常]
            if (StrUtil.equals(ScorpioConfig.PAGE_NAME_HTML_EXCEPTION, fileName)) {
                throw new ScorpioNormalException(ExceptionMessage.CREATED_EXCEPTION);
            }
            // 读取文件内容
            byte[] fileContent = FileUtil.readBytes(file);
            // 设置响应对象的contentType
            String fileExtName = FileUtil.extName(file);
            String mimeType = AppXMLParser.getMimeType(fileExtName);
            response.setContentType(mimeType);
            // 写入响应对象
            response.setBody(fileContent);
            this.reply(socket, response, ResponseStatus._200);
        } else {
            // 写入[文件未找到信息]
            String responseMessage = StrUtil.format(ResponseConstant.RESPONSE_404_HTML, uri, uri);
            response.getWriter().println(responseMessage);
            this.reply(socket, response, ResponseStatus._404);
        }
    }

    /**
     * 响应[404/200]
     *
     * @param socket         客户端连接
     * @param responseStatus 响应状态
     * @param response       响应对象
     * @throws IOException IO异常
     */
    public void reply(Socket socket, Response response, ResponseStatus responseStatus)
            throws IOException, ScorpioNormalException {
        // 根据响应类型构建响应头
        String header;
        switch (responseStatus) {
            case _200:
                header = StrUtil.format(ResponseConstant.RESPONSE_200_HEADER, response.getContentType());
                this.reply(socket, response, header);
                break;
            case _404:
                header = ResponseConstant.RESPONSE_404_HEADER;
                this.reply(socket, response, header);
                break;
            default:
                throw new ScorpioNormalException(ExceptionMessage.RESPONSE_STATUS_NOT_EXIST_EXCEPTION);
        }
    }

    /**
     * 响应
     *
     * @param socket   客户端连接
     * @param header   响应头
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
     * 响应[500]
     *
     * @param socket    客户端连接
     * @param exception 异常
     */
    public void reply(Socket socket, Response response, Exception exception) throws IOException {
        // 拿到异常的堆栈数组
        StackTraceElement[] stackTraceElement = exception.getStackTrace();
        // 循环拼接方法调用时产生的异常堆栈信息
        StringBuilder stringBuilder = new StringBuilder();
        // 先拼接主要的错误信息
        stringBuilder.append(exception);
        stringBuilder.append(ScorpioConfig.SYMBOL_R_N);
        // 遍历异常堆栈数组,循环拼接异常信息
        for (StackTraceElement traceElement : stackTraceElement) {
            stringBuilder.append(ScorpioConfig.SYMBOL_TAB);
            stringBuilder.append(traceElement.toString());
            stringBuilder.append(ScorpioConfig.SYMBOL_R_N);
        }
        // 异常提示信息拼接
        String message = exception.getMessage();
        if (StrUtil.isNotEmpty(message) && message.length() > 20) {
            // 提示信息太长,只取一部分
            message = message.substring(0, 19);
        }
        // 写入Response组件Body信息
        String responseMessage = StrUtil.format(ResponseConstant.RESPONSE_500_HTML,
                message, exception.toString(), stringBuilder.toString());
        response.getWriter().println(responseMessage);
        String header = ResponseConstant.RESPONSE_500_HEADER;
        // 响应
        this.reply(socket, response, header);
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
     *
     * @param request request 组件
     */
    public void requestLog(Request request) {
        LogFactory.get().info("Scorpio收到请求：\n" + request.getRequestContent());
    }

    /**
     * Response组件响应信息
     *
     * @param response response 组件
     */
    public void responseLog(Response response) {
        LogFactory.get().info("Scorpio进行应答: " + new String(response.getBody(), StandardCharsets.UTF_8));
    }
}
















