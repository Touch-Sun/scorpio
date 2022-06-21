package com.touchsun.scorpio.core.config;

import cn.hutool.system.SystemUtil;

import java.io.File;

/**
 * 启动配置项
 * @author Lee
 */
public class ScorpioConfig {

    /**
     * 默认监听端口号[自动测试/常规配置]
     */
    public static Integer DEFAULT_PORT = 1204;

    /**
     * 默认地址[自动测试/常规配置]
     */
    public static String DEFAULT_ADDRESS = "127.0.0.1";

    /**
     * URI[根路径]
     */
    public static String URI_ROOT = "/";

    /**
     * 信息[文件未找到]
     */
    public static String MSG_FILE_NOT_FOUND = "File Not Found";

    /**
     * 信息[欢迎Scorpio]
     */
    public static final String MSG_WELCOME = "Hi, Welcome to Scorpio!";

    /**
     * 静态文件路径[WEBAPPS_FOLDER]
     */
    public final static File WEBAPPS_FOLDER = new File(SystemUtil.get("user.dir"), "webapps");

    /**
     * 静态文件路径[ROOT_FOLDER]
     */
    public final static File ROOT_FOLDER = new File(WEBAPPS_FOLDER, "ROOT");
}
