package com.touchsun.scorpio.core.config;

import cn.hutool.core.date.DateUtil;
import cn.hutool.system.SystemUtil;
import com.touchsun.scorpio.core.type.LoadStrategy;

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
     * 默认请求协议[自动测试/常规配置]
     */
    public static final String SCORPIO_PROTOCOL = "http://";

    /**
     * 默认地址[自动测试/常规配置]
     */
    public static String DEFAULT_ADDRESS = "127.0.0.1";

    /**
     * 默认线程名称[常规配置]
     */
    public static final String DEFAULT_THREAD_NAME = "scorpio-thread-";

    /**
     * 默认配置文件元素节点名称[Context]
     */
    public static final String DEFAULT_SERVER_XML_ELEMENT_NAME_CONTEXT = "Context";

    /**
     * 默认配置文件元素节点名称[Host]
     */
    public static final String DEFAULT_SERVER_XML_ELEMENT_NAME_HOST = "Host";

    /**
     * 默认配置文件元素节点名称[Service]
     */
    public static final String DEFAULT_SERVER_XML_ELEMENT_NAME_SERVICE = "Service";

    /**
     * 默认配置文件元素节点名称[Engine]
     */
    public static final String DEFAULT_SERVER_XML_ELEMENT_NAME_ENGINE = "Engine";

    /**
     * 默认配置文件元素节点属性名称[path]
     */
    public static final String DEFAULT_SERVER_XML_ELEMENT_ATTRIBUTE_PATH_NAME = "path";

    /**
     * 默认配置文件元素节点属性名称[appPath]
     */
    public static final String DEFAULT_SERVER_XML_ELEMENT_ATTRIBUTE_APP_PATH_NAME = "appPath";

    /**
     * 默认配置文件元素节点属性名称[name]
     */
    public static final String DEFAULT_SERVER_XML_ELEMENT_ATTRIBUTE_NAME_NAME = "name";

    /**
     * 默认配置文件元素节点属性名称[defaultHost]
     */
    public static final String DEFAULT_SERVER_XML_ELEMENT_ATTRIBUTE_DEFAULT_HOST_NAME = "defaultHost";

    /**
     * 默认程序加载策略[ALL]
     * 详细信息参照 @LoadStrategy
     */
    public static final LoadStrategy DEFAULT_LOAD_CONTEXT_STRATEGY = LoadStrategy.ALL;

    /**
     * URI[根路径]
     */
    public static String URI_ROOT = "/";

    /**
     * 文件夹名称[ROOT文件夹]
     */
    public static final String FOLDER_ROOT_NAME = "ROOT";

    /**
     * 文件名称[server.xml]
     */
    public static final String SERVER_XML_NAME = "server.xml";

    /**
     * 页面[timeConsume.html]
     */
    public static final String PAGE_NAME_HTML_TIME_CONSUME = "timeConsume.html";

    /**
     * 信息[文件未找到]
     */
    public static String MSG_FILE_NOT_FOUND = "File Not Found";

    /**
     * 信息[欢迎Scorpio]
     */
    public static final String MSG_WELCOME = "Hi, Welcome to Scorpio!";

    /**
     * 信息[Web应用部署成功]
     */
    public static final String MSG_DEPLOY_FINISH = "Scorpio's web application has been deployed at path [{}], and the access path is {}{}:{}{}, which takes {} milliseconds in total";

    /**
     * 信息[加载Web应用个数,从webApps目录下]
     */
    public static final String MSG_LOAD_APP_CONTEXT_COUNT_WEB_APP_FOLDER = "load application {} into context from \"webApps\" folder";

    /**
     * 信息[加载Web应用个数,从server.xml配置中]
     */
    public static final String MSG_LOAD_APP_CONTEXT_COUNT_SERVER_XML = "load application {} into context from \"server.xml\" config";

    /**
     * 信息[Scorpio启动成功]
     */
    public static final String MSG_SCORPIO_STARTED = "Scorpio started successfully, listening on port [io-{}]";

    /**
     * 信息[修正Uri]
     */
    public static final String MSG_FIX_URI_APP = "URI correction successful [{}] -> [{}]";

    /**
     * 信息[ROOT程序不修正Uri]
     */
    public static final String MSG_FIX_URI_ROOT_APP = "Access the ROOT app without corrections";

    /**
     * 符号[\t\t]
     */
    public static final String SYMBOL_TAB_TAB = "\t\t";

    /**
     * 符号[:]
     */
    public static final String SYMBOL_COLON = ":";

    /**
     * Web程序存放目录[WEBAPPS_FOLDER]
     */
    public final static File WEBAPPS_FOLDER = new File(SystemUtil.get("user.dir"), "webapps");

    /**
     * 根Web程序的目录[ROOT_FOLDER]
     */
    public final static File ROOT_FOLDER = new File(WEBAPPS_FOLDER, "ROOT");

    /**
     * 配置文件存放目录[config]
     */
    public final static File  CONFIG_FOLDER = new File(SystemUtil.get("user.dir"), "config");

    /**
     * 配置文件[server.xml]
     */
    public final static File SERVER_XML_FILE = new File(CONFIG_FOLDER, SERVER_XML_NAME);

    ///////////////////////////////////////////////////////////////////////////
    // Scorpio虚拟机信息
    //////////////////////////////////////////////////////////////////////////
    public static final String JVM_SERVER_VERSION_FILED = "Server Version";
    public static final String JVM_SERVER_VERSION_VALUE = "Scorpio/0.0.1";

    public static final String JVM_SERVER_BUILD_TIME_FIELD = "Server Build";
    public static final String JVM_SERVER_BUILD_TIME_VALUE = DateUtil.now();

    public static final String JVM_OS_NAME_FIELD = "OS Name";
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

























