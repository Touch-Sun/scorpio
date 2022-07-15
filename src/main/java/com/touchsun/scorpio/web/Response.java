package com.touchsun.scorpio.web;

import cn.hutool.log.LogFactory;
import com.touchsun.scorpio.constant.ResponseConstant;
import lombok.Getter;
import lombok.Setter;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

/**
 * 响应对象
 *
 * @author Lee
 */
public class Response extends BaseResponse {

    /**
     * 存放HTML文本
     */
    private StringWriter stringWriter;

    /**
     * 用于写入数据到[stringWriter]
     * 模仿Tomcat中<code>response.getWriter().println();</code>风格代码
     * 此成员的个get方法重写了HttpServletResponse的getWriter，多态形式展现
     */
    private PrintWriter writer;

    /**
     * 响应数据的类型
     */
    @Setter
    @Getter
    private String contentType;

    /**
     * 存放二进制文件
     */
    @Setter
    private byte[] body;

    public Response() {
        this.stringWriter = new StringWriter();
        this.writer = new PrintWriter(stringWriter);
        this.contentType = ResponseConstant.TEXT_HTML;
        LogFactory.get().debug(this.toString());
    }

    /**
     * 得到HTML的字节数组
     *
     * @return byte[]
     */
    public byte[] getBody() {
        if (null != this.body) {
            // body不为空时,证明是二进制文件的请求,直接返回body
            return body;
        }
        String content = stringWriter.toString();
        return content.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public PrintWriter getWriter() {
        return writer;
    }
}
