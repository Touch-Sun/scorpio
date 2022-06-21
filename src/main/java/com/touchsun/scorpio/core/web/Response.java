package com.touchsun.scorpio.core.web;

import com.touchsun.scorpio.core.constant.ResponseConstant;
import lombok.Getter;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

/**
 * 响应对象
 * @author Lee
 */
public class Response {

    /**
     * 存放HTML文本
     */
    private StringWriter stringWriter;

    /**
     * 用于写入数据到[stringWriter]
     * 模仿Tomcat中<code>response.getWriter().println();</code>风格代码
     */
    @Getter
    private PrintWriter printWriter;

    /**
     * 响应数据的类型
     */
    private String contentType;

    public Response() {
        this.stringWriter = new StringWriter();
        this.printWriter = new PrintWriter(stringWriter);
        this.contentType = ResponseConstant.TEXT_HTML;
        System.out.println("Response实例化成功 [" + this + "]");
    }

    /**
     * 得到HTML的字节数组
     * @return byte[]
     */
    public byte[] getBody() {
        String content = stringWriter.toString();
        return content.getBytes(StandardCharsets.UTF_8);
    }
}
