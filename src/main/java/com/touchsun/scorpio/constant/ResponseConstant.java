package com.touchsun.scorpio.constant;

/**
 * 响应信息常量
 * @author Lee
 */
public class ResponseConstant {

    /**
     * 响应头[200]
     */
    public static final String RESPONSE_200_HEADER = "HTTP/1.1 200 OK\r\nContent-Type: {}\r\n\r\n";

    /**
     * 响应头[404]
     */
    public static final String RESPONSE_404_HEADER = "HTTP/1.1 404 Not Found\r\nContent-Type: text/html\r\n\r\n";

    /**
     * 响应信息HTML[404]
     */
    public static final String RESPONSE_404_HTML = "<html><head><title>Scorpio / 0.0.1 - Error report</title><style>" +
            "<!--H1 {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;font-size:22px;} " +
            "H2 {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;font-size:16px;} " +
            "H3 {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;font-size:14px;} " +
            "BODY {font-family:Tahoma,Arial,sans-serif;color:black;background-color:white;} " +
            "B {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;} " +
            "P {font-family:Tahoma,Arial,sans-serif;background:white;color:black;font-size:12px;}" +
            "A {color : black;}A.name {color : black;}HR {color : #525D76;}--></style> " +
            "</head><body><h1>HTTP Status 404 - {}</h1>" +
            "<HR size='1' noshade='noshade'><p><b>type</b> Status report</p><p><b>message</b> <u>{}</u></p><p><b>description</b> " +
            "<u>The requested resource is not available.</u></p><HR size='1' noshade='noshade'><h3>Scorpio 0.0.1</h3>" +
            "</body></html>";

    /**
     * Content-Type[text/html]
     */
    public static final String TEXT_HTML = "text/html";

    /**
     * Content-Type[application/json]
     */
    public static final String APPLICATION_JSON = "application/json";
}
