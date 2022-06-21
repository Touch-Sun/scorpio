package com.touchsun.scorpio.core.config;

import cn.hutool.core.date.DateUtil;
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
     * 符号[\t\t]
     */
    public static final String SYMBOL_TAB_TAB = "\t\t";

    /**
     * 符号[:]
     */
    public static final String SYMBOL_COLON = ":";

    /**
     * 静态文件路径[WEBAPPS_FOLDER]
     */
    public final static File WEBAPPS_FOLDER = new File(SystemUtil.get("user.dir"), "webapps");

    /**
     * 静态文件路径[ROOT_FOLDER]
     */
    public final static File ROOT_FOLDER = new File(WEBAPPS_FOLDER, "ROOT");

    ///////////////////////////////////////////////////////////////////////////
    // Scorpio虚拟机信息
    public static final String JVM_SERVER_VERSION_FILED = "Server Version";
    public static final String JVM_SERVER_VERSION_VALUE = "Scorpio/0.0.1";

    public static final String JVM_SERVER_BUILD_TIME_FIELD = "Server Build";
    public static final String JVM_SERVER_BUILD_TIME_VALUE = DateUtil.now();

    public static final String JVM_OS_NAME_FIELD = "OS Name\t";
    public static final String JVM_OS_NAME_VALUE = SystemUtil.get("os.name");

    public static final String JVM_OS_VERSION_FIELD = "OS Version";
    public static final String JVM_OS_VERSION_VALUE = SystemUtil.get("os.version");

    public static final String JVM_OS_ARCHITECTURE_FIELD = "OS Architecture";
    public static final String JVM_OS_ARCHITECTURE_VALUE = SystemUtil.get("os.arch");

    public static final String JVM_JAVA_HOME_FIELD = "Java Home";
    public static final String JVM_JAVA_HOME_VALUE = SystemUtil.get("java.home");

    public static final String JVM_JAVA_VERSION_FIELD = "Java Version";
    public static final String JVM_JAVA_VERSION_VALUE = SystemUtil.get("java.version");

    public static final String JVM_JAVA_VENDOR_FIELD = "Java Vendor";
    public static final String JVM_JAVA_VENDOR_VALUE = SystemUtil.get("java.vm.specification.vendor");
    ///////////////////////////////////////////////////////////////////////////


}

























