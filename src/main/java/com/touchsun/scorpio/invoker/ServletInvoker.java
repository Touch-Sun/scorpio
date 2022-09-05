package com.touchsun.scorpio.invoker;

import cn.hutool.core.util.ReflectUtil;
import com.touchsun.scorpio.core.Context;
import com.touchsun.scorpio.type.ResponseStatus;
import com.touchsun.scorpio.web.Request;
import com.touchsun.scorpio.web.Response;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet执行器
 * - 单例模式
 *
 * @author lee
 */
public class ServletInvoker extends HttpServlet {

    private ServletInvoker() {

    }

    private static ServletInvoker instance = new ServletInvoker();

    public static synchronized ServletInvoker getInstance() {
        return instance;
    }


    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 转换到Scorpio的Request/Response
        Request request = (Request) req;
        Response response = (Response) resp;

        // 获取 URI
        String uri = request.getUri();
        // 获取上下文
        Context context = request.getAppContext();
        // 获取ClassName
        String servletClassName = context.getServletClassName(uri);

        // 反射实例化Servlet
        Object servlet = ReflectUtil.newInstance(servletClassName);
        // 反射执行对应Servlet的service方法, 且把Scorpio的Request传入
        ReflectUtil.invoke(servlet, "service", request, response);

        // 处理结束,响应200
        response.setStatus(ResponseStatus._200.getCode());

    }
}
