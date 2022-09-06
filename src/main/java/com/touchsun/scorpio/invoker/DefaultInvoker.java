package com.touchsun.scorpio.invoker;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 静态资源执行器
 * - 单例模式
 *
 * @author lee
 */
public class DefaultInvoker extends HttpServlet {

    private static DefaultInvoker instance = new DefaultInvoker();

    public static DefaultInvoker getInstance() {
        return instance;
    }

    private DefaultInvoker() {

    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 交给Core处理
    }
}
