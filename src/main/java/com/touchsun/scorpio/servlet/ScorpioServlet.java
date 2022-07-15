package com.touchsun.scorpio.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Scorpio Servlet
 *
 * @author Lee
 */
public class ScorpioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.getWriter().println("<h1 style=\"color: #897623\">Hello Scorpio Servlet!</h1>");
    }
}
