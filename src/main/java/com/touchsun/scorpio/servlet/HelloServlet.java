package com.touchsun.scorpio.servlet;

import cn.hutool.log.LogFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Scorpio的第一个Servlet
 *
 * @author Lee
 */
public class HelloServlet extends HttpServlet {

    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.getWriter().println("<h1>Hello Scorpio, I am your first servlet!</h1>");
        } catch (IOException e) {
            e.printStackTrace();
            LogFactory.get().error(e);
        }
    }
}
