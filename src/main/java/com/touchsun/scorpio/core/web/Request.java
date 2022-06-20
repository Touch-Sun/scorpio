package com.touchsun.scorpio.core.web;

import cn.hutool.core.util.StrUtil;
import com.touchsun.scorpio.core.plugin.VirtualBrowser;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Http请求对象
 * @author Lee
 */
public class Request {

    /**
     * 请求内容[全部Http内容]
     */
    @Getter
    private String requestContent;

    /**
     * 统一资源标识
     */
    @Getter
    private String uri;
    /**
     * socket
     */
    private Socket socket;

    public Request(Socket socket) throws IOException {
        this.socket = socket;
        parseHttpRequest();
        if (!StrUtil.isEmpty(requestContent)) {
            parseUri();
        } else {
            System.err.println("Http请求内容为空,无法解析Uri");
            return;
        }
        System.out.println("Request实例化成功" + this);
    }

    /**
     * 解析Http内容
     * @throws IOException
     */
    private void parseHttpRequest() throws IOException {
        InputStream inputStream = this.socket.getInputStream();
        byte[] result = VirtualBrowser.readBytes(inputStream);
        this.requestContent = new String(result, StandardCharsets.UTF_8);
    }

    /**
     * 解析Uri
     */
    private void parseUri() {
        String temp;
        temp = StrUtil.subBetween(requestContent, " ", " ");
        // 包含参数判断
        if (!StrUtil.contains(temp, '?')) {
            uri = temp;
        } else {
            temp = StrUtil.subBefore(temp, '?', false);
            uri = temp;
        }
    }

}



















