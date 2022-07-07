package com.touchsun.scorpio.web;

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.LogFactory;
import com.touchsun.scorpio.config.ScorpioConfig;
import com.touchsun.scorpio.core.Context;
import com.touchsun.scorpio.core.Engine;
import com.touchsun.scorpio.core.Service;
import com.touchsun.scorpio.plugin.VirtualBrowser;
import lombok.Getter;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

/**
 * Http请求对象
 *
 * @author Lee
 */
public class Request implements HttpServletRequest {

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

    /**
     * Scorpio服务
     */
    private Service service;

    /**
     * 应用程序上下文
     */
    @Getter
    private Context appContext;

    public Request(Socket socket, Service service) throws IOException {
        this.socket = socket;
        this.service = service;
        parseHttpRequest();
        if (!StrUtil.isEmpty(requestContent)) {
            // 解析URI
            parseUri();
            // 初始化应用上下文
            initContext();
            // 修正Uri
            fixUri();
        } else {
            System.err.println("Http请求内容为空,无法解析Uri");
            return;
        }
        LogFactory.get().debug(this.toString());
    }

    /**
     * 解析Http内容
     *
     * @throws IOException
     */
    private void parseHttpRequest() throws IOException {
        InputStream inputStream = this.socket.getInputStream();
        byte[] result = VirtualBrowser.readBytes(inputStream, false);
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

    /**
     * 修正Uri "/numbers/index.html" -> "/index.html"
     */
    private void fixUri() {
        if (!StrUtil.equals(ScorpioConfig.URI_ROOT, appContext.getPath())) {
            // 若访问地址,不是根,而是其他Web程序,则需要修正Uri
            String oldUri = uri;
            uri = StrUtil.removePrefix(uri, appContext.getPath());
            if (StrUtil.isEmpty(uri)) {
                // 访问应用,不访问具体文件时[/numbers],uri被修正为[''],[/numbers -> '']
                uri = ScorpioConfig.URI_ROOT;
            }
            LogFactory.get().debug(ScorpioConfig.MSG_FIX_URI_APP, oldUri, uri);
        } else {
            LogFactory.get().debug(ScorpioConfig.MSG_FIX_URI_ROOT_APP);
        }
    }

    /**
     * 初始化应用上下文
     */
    private void initContext() {
        // 从Scorpio服务中拿出Engine引擎处理
        Engine engine = this.service.getEngine();
        // 先通过uri获取Context[http://127.0.0.1/numbers情况 -> /numbers]
        appContext = engine.getDefaultHost().getContext(uri);
        if (appContext != null) {
            // 不为空,代表访问的是一个应用,但是没有指名具体的访问文件
            return;
        }
        // 在URI中获取Path[Context的Key] -> http://127.0.0.1/numbers/index.html -> [numbers]
        String path = StrUtil.subBetween(uri, ScorpioConfig.URI_ROOT, ScorpioConfig.URI_ROOT);
        if (null == path) {
            // 表示根
            path = ScorpioConfig.URI_ROOT;
        } else {
            // path -> ["/" + "numbers"] -> [/numbers]
            path = ScorpioConfig.URI_ROOT + path;
        }
        // 获取上下文实例
        appContext = engine.getDefaultHost().getContext(path);
        if (null == appContext) {
            // 未获取到,直接使用根[ROOT]
            appContext = engine.getDefaultHost().getContext(ScorpioConfig.URI_ROOT);
        }
    }

    @Override
    public String getAuthType() {
        return null;
    }

    @Override
    public Cookie[] getCookies() {
        return new Cookie[0];
    }

    @Override
    public long getDateHeader(String s) {
        return 0;
    }

    @Override
    public String getHeader(String s) {
        return null;
    }

    @Override
    public Enumeration<String> getHeaders(String s) {
        return null;
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        return null;
    }

    @Override
    public int getIntHeader(String s) {
        return 0;
    }

    @Override
    public String getMethod() {
        return null;
    }

    @Override
    public String getPathInfo() {
        return null;
    }

    @Override
    public String getPathTranslated() {
        return null;
    }

    @Override
    public String getContextPath() {
        return null;
    }

    @Override
    public String getQueryString() {
        return null;
    }

    @Override
    public String getRemoteUser() {
        return null;
    }

    @Override
    public boolean isUserInRole(String s) {
        return false;
    }

    @Override
    public Principal getUserPrincipal() {
        return null;
    }

    @Override
    public String getRequestedSessionId() {
        return null;
    }

    @Override
    public String getRequestURI() {
        return null;
    }

    @Override
    public StringBuffer getRequestURL() {
        return null;
    }

    @Override
    public String getServletPath() {
        return null;
    }

    @Override
    public HttpSession getSession(boolean b) {
        return null;
    }

    @Override
    public HttpSession getSession() {
        return null;
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromCookie() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromUrl() {
        return false;
    }

    @Override
    public boolean authenticate(HttpServletResponse httpServletResponse) throws IOException, ServletException {
        return false;
    }

    @Override
    public void login(String s, String s1) throws ServletException {

    }

    @Override
    public void logout() throws ServletException {

    }

    @Override
    public Collection<Part> getParts() throws IOException, IllegalStateException, ServletException {
        return null;
    }

    @Override
    public Part getPart(String s) throws IOException, IllegalStateException, ServletException {
        return null;
    }

    @Override
    public Object getAttribute(String s) {
        return null;
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return null;
    }

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public void setCharacterEncoding(String s) throws UnsupportedEncodingException {

    }

    @Override
    public int getContentLength() {
        return 0;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return null;
    }

    @Override
    public String getParameter(String s) {
        return null;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return null;
    }

    @Override
    public String[] getParameterValues(String s) {
        return new String[0];
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return null;
    }

    @Override
    public String getProtocol() {
        return null;
    }

    @Override
    public String getScheme() {
        return null;
    }

    @Override
    public String getServerName() {
        return null;
    }

    @Override
    public int getServerPort() {
        return 0;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return null;
    }

    @Override
    public String getRemoteAddr() {
        return null;
    }

    @Override
    public String getRemoteHost() {
        return null;
    }

    @Override
    public void setAttribute(String s, Object o) {

    }

    @Override
    public void removeAttribute(String s) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }

    @Override
    public Enumeration<Locale> getLocales() {
        return null;
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String s) {
        return null;
    }

    @Override
    public String getRealPath(String s) {
        return null;
    }

    @Override
    public int getRemotePort() {
        return 0;
    }

    @Override
    public String getLocalName() {
        return null;
    }

    @Override
    public String getLocalAddr() {
        return null;
    }

    @Override
    public int getLocalPort() {
        return 0;
    }

    @Override
    public ServletContext getServletContext() {
        return null;
    }

    @Override
    public AsyncContext startAsync() {
        return null;
    }

    @Override
    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) {
        return null;
    }

    @Override
    public boolean isAsyncStarted() {
        return false;
    }

    @Override
    public boolean isAsyncSupported() {
        return false;
    }

    @Override
    public AsyncContext getAsyncContext() {
        return null;
    }

    @Override
    public DispatcherType getDispatcherType() {
        return null;
    }
}



















