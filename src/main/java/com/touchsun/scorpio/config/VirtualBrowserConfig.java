package com.touchsun.scorpio.config;

/**
 * 虚拟浏览器配置
 *
 * @author Lee
 */
public class VirtualBrowserConfig {

    /**
     * 默认访问地址
     */
    public static final String DEFAULT_URL = "https://cn.bing.com/";

    /**
     * 默认的Http端口80标识
     */
    public static final int DEFAULT_HTTP_PORT_TAG = -1;

    /**
     * 输入流没有值标志
     */
    public static final int DEFAULT_INPUT_STREAM_NULL_TAG = -1;

    /**
     * 符号[换行]
     */
    public static final String SYMBOL_NEWLINE = "\r\n";

    /**
     * 符号[空格]
     */
    public static final String SYMBOL_SPACE = " ";

    /**
     * 符号[冒号]
     */
    public static final String SYMBOL_COLON = ":";

    /**
     * 根路径
     */
    public static final String PATH_ROOT = "/";

    /**
     * HTTP请求头[Host]
     */
    public static final String REQUEST_HEADER_HOST = "Host";

    /**
     * HTTP请求头[Accept]
     */
    public static final String REQUEST_HEADER_ACCEPT = "Accept";
    public static final String REQUEST_HEADER_ACCEPT_TEXT_HTML = "text/html";

    /**
     * HTTP请求头[Connection]
     */
    public static final String REQUEST_HEADER_CONNECTION = "Connection";
    public static final String REQUEST_HEADER_CONNECTION_CLOSE = "close";

    /**
     * HTTP请求头[User-Agent]
     */
    public static final String REQUEST_HEADER_USER_AGENT = "User-Agent";
    public static final String REQUEST_HEADER_USER_AGENT_NAME = "Virtual Browser / Scorpio 0.0.1";

    /**
     * HTTP请求头[Accept-Encoding]
     */
    public static final String REQUEST_HEADER_ACCEPT_ENCODING = "Accept-Encoding";
    public static final String REQUEST_HEADER_ACCEPT_ENCODING_GZIP = "gzip";

    /**
     * Http请求方法类型
     */
    public static final String HTTP_METHOD_GET = "GET";

    /**
     * Http版本信息
     */
    public static final String HTTP_VERSION = "HTTP/1.1";

}
