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
    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.getWriter().println("Hello Scorpio, I am your first servlet!");
        } catch (IOException e) {
            e.printStackTrace();
            LogFactory.get().error(e);
        }
    }

    public static HelloServlet instance() {
        return new HelloServlet();
    }
}
