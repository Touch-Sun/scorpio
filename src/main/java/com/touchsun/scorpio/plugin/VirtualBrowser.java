package com.touchsun.scorpio.plugin;

import com.touchsun.scorpio.config.VirtualBrowserConfig;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 虚拟浏览器
 * 为了方便调试Http
 * @author Lee
 */
public class VirtualBrowser {
    public static void main(String[] args) {
        run();
    }

    private static void run() {
        String contentText = getContent(VirtualBrowserConfig.DEFAULT_URL);
        String httpContentText = getHttpContent(VirtualBrowserConfig.DEFAULT_URL);

        System.out.println("--------------------------[HTTP响应全部信息]--------------------------");
        System.out.println(httpContentText);
        System.out.println("--------------------------[HTTP响应内容部分]--------------------------");
        System.out.println(contentText);
    }


    /**
     * [重载方法]
     * 获取Http内部的内容
     * @param url URL
     * @return String类型信息
     */
    public static String getContent(String url) {
        return getContent(url, false);
    }

    /**
     * [重载方法]
     * 获取Http内部的内容
     * @param url URL
     * @param gzip 是否gzip
     * @return String类型信息
     */
    public static String getContent(String url, boolean gzip) {
        byte[] result = getContentBytes(url, gzip);
        if (result == null) {
            return null;
        }
        return new String(result, StandardCharsets.UTF_8).trim();
    }

    /**
     * [重载方法]
     * 获取Http全部信息
     * @param url URL
     * @return String类型信息
     */
    public static String getHttpContent(String url) {
        return getHttpContent(url, false);
    }

    /**
     * [重载方法]
     * 获取Http全部信息
     * @param url URL
     * @param gzip 是否gzip
     * @return String类型信息
     */
    public static String getHttpContent(String url, boolean gzip) {
        byte[] bytes = getHttpBytes(url, gzip);
        return new String(bytes).trim();
    }

    /**
     * [重载方法]
     * 获取Http内部内容的字节信息
     * @param url URL
     * @return 字节数组类型信息
     */
    public static byte[] getContentBytes(String url) {
        return getContentBytes(url, false);
    }

    /**
     * [重载方法]
     * 获取Http内部内容的字节信息
     * @param url URL
     * @param gzip 是否gzip
     * @return 字节数组类型信息
     */
    public static byte[] getContentBytes(String url, boolean gzip) {
        // 得到Http响应信息
        byte[] response = getHttpBytes(url, gzip);
        byte[] doubleNewline = (VirtualBrowserConfig.SYMBOL_NEWLINE
                + VirtualBrowserConfig.SYMBOL_NEWLINE).getBytes();

        // 遍历所有内容
        int position = -1;
        int size = response.length - doubleNewline.length;
        for (int i = 0; i < size; i++) {
            byte[] temp = Arrays.copyOfRange(response, i, i + doubleNewline.length);
            if (Arrays.equals(temp, doubleNewline)) {
                position = i;
                break;
            }
        }

        if (-1 == position) {
            return null;
        }

        position += doubleNewline.length;

        return Arrays.copyOfRange(response ,position, response.length);
    }

    /**
     * [重载方法]
     * 获取Http全部内容的字节信息
     * @param url URL
     * @return 字节数组类型信息
     */
    public static byte[] getHttpBytes(String url) {
        return getHttpBytes(url, false);
    }

    /**
     * [重载方法]
     * 获取Http全部内容的字节信息
     * @param url URL
     * @param gzip 是否gzip
     * @return 字节数组类型信息
     */
    public static byte[] getHttpBytes(String url, boolean gzip) {
        byte[] result = null;
        try {
            // 解析URL
            URL u = new URL(url);
            Socket client = new Socket();
            int port = u.getPort();
            if (VirtualBrowserConfig.DEFAULT_HTTP_PORT_TAG == port) {
                port = 80;
            }

            // 建立连接
            InetSocketAddress inetSocketAddress = new InetSocketAddress(u.getHost(), port);
            client.connect(inetSocketAddress, 1000);

            // 准备请求头
            Map<String, String> requestHeaders = new HashMap<>(20);
            requestHeaders.put(VirtualBrowserConfig.REQUEST_HEADER_HOST,
                    u.getHost() + VirtualBrowserConfig.SYMBOL_COLON + port);
            requestHeaders.put(VirtualBrowserConfig.REQUEST_HEADER_ACCEPT,
                    VirtualBrowserConfig.REQUEST_HEADER_ACCEPT_TEXT_HTML);
            requestHeaders.put(VirtualBrowserConfig.REQUEST_HEADER_CONNECTION,
                    VirtualBrowserConfig.REQUEST_HEADER_CONNECTION_CLOSE);
            requestHeaders.put(VirtualBrowserConfig.REQUEST_HEADER_USER_AGENT,
                    VirtualBrowserConfig.REQUEST_HEADER_USER_AGENT_NAME);
            if (gzip) {
                requestHeaders.put(VirtualBrowserConfig.REQUEST_HEADER_ACCEPT_ENCODING,
                        VirtualBrowserConfig.REQUEST_HEADER_ACCEPT_ENCODING_GZIP);
            }

            // 处理Http配置
            String path = u.getPath();
            if (path.length() == 0) {
                path = VirtualBrowserConfig.PATH_ROOT;
            }
            String firstLine = VirtualBrowserConfig.HTTP_METHOD_GET
                    + VirtualBrowserConfig.SYMBOL_SPACE
                    + path
                    + VirtualBrowserConfig.SYMBOL_SPACE
                    + VirtualBrowserConfig.HTTP_VERSION
                    + VirtualBrowserConfig.SYMBOL_NEWLINE;

            // 填补Http请求信息
            StringBuffer httpRequestContent = new StringBuffer();
            httpRequestContent.append(firstLine);
            Set<String> headers = requestHeaders.keySet();
            for (String header : headers) {
                String headerLine = header + VirtualBrowserConfig.SYMBOL_COLON
                        + VirtualBrowserConfig.SYMBOL_SPACE
                        + requestHeaders.get(header)
                        + VirtualBrowserConfig.SYMBOL_NEWLINE;
                httpRequestContent.append(headerLine);
            }

            /* System.err.println(httpRequestContent); // 打印Http协议信息*/

            // 准备数据传输
            PrintWriter printWriter = new PrintWriter(client.getOutputStream(), true);
            printWriter.println(httpRequestContent);
            InputStream inputStream = client.getInputStream();
            // 读取Http请求内容的所有字节
            result = readBytes(inputStream);
            client.close();

        } catch (Exception exception) {
            exception.printStackTrace();
            result = exception.toString().getBytes(StandardCharsets.UTF_8);
        }

        return result;
    }

    /**
     * 读取字节内容从请求中
     * @param inputStream 客户端创建的输入流
     * @return 字节内容
     * @throws IOException IO异常
     */
    public static byte[] readBytes(InputStream inputStream) throws IOException {
        int bufferSize = 1024;

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[bufferSize];
        while (true) {
            int length = inputStream.read(buffer);
            if (VirtualBrowserConfig.DEFAULT_INPUT_STREAM_NULL_TAG == length) {
                break;
            }
            byteArrayOutputStream.write(buffer, 0 ,length);
            if (length != bufferSize) {
                break;
            }
        }

        return byteArrayOutputStream.toByteArray();
    }
}






















