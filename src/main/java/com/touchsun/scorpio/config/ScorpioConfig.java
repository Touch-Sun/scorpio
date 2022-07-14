package com.touchsun.scorpio.config;

import cn.hutool.core.date.DateUtil;
import cn.hutool.system.SystemUtil;
import com.touchsun.scorpio.type.LoadStrategy;
import com.touchsun.scorpio.type.WelcomeStrategy;

import java.io.File;

/**
 * 启动配置项
 *
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
     * Web请求线程名称[常规配置]
     */
    public static final String WEB_THREAD_NAME = "scorpio-web-thread-";

    /**
     * Scorpio启动实例请求线程名称[常规配置]
     */
    public static final String SCORPIO_THREAD_NAME = "scorpio-started-thread-";

    /**
     * 默认web.xml配置文件地址[WEB-INF/web.xml]
     */
    public static final String DEFAULT_WEB_XML_PATH = "WEB-INF/web.xml";

    /**
     * 默认配置文件元素节点名称[Context]
     */
    public static final String DEFAULT_SERVER_XML_ELEMENT_NAME_CONTEXT = "Context";

    /**
     * 默认配置文件元素节点名称[Host]
     */
    public static final String DEFAULT_SERVER_XML_ELEMENT_NAME_HOST = "Host";

    /**
     * 默认配置文件元素节点名称[Connector]
     */
    public static final String DEFAULT_SERVER_XML_ELEMENT_NAME_CONNECTOR = "Connector";

    /**
     * 默认配置文件元素节点名称[welcome-file]
     */
    public static final String DEFAULT_SERVER_XML_ELEMENT_NAME_WELCOME_FILE = "welcome-file";

    /**
     * 默认配置文件元素节点名称[mime-mapping]
     */
    public static final String DEFAULT_SERVER_XML_ELEMENT_NAME_MIME_MAPPING = "mime-mapping";

    /**
     * 默认配置文件元素节点名称[extension]
     */
    public static final String DEFAULT_SERVER_XML_ELEMENT_NAME_EXTENSION = "extension";

    /**
     * 默认配置文件元素节点名称[mimeType]
     */
    public static final String DEFAULT_SERVER_XML_ELEMENT_NAME_MIMETYPE = "mime-type";

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
     * 默认配置文件元素节点属性名称[port]
     */
    public static final String DEFAULT_SERVER_XML_ELEMENT_ATTRIBUTE_PORT_NAME = "port";

    /**
     * 默认配置文件元素节点属性名称[defaultHost]
     */
    public static final String DEFAULT_SERVER_XML_ELEMENT_ATTRIBUTE_DEFAULT_HOST_NAME = "defaultHost";

    /**
     * Servlet配置文件元素节点名称[WatchedResource]
     */
    public static final String DEFAULT_CONTENT_XML_ELEMENT_NAME_WATCHEDRESOURCE = "WatchedResource";

    /**
     * 默认程序加载策略[ALL]
     * 详细信息参照 @LoadStrategy
     */
    public static final LoadStrategy DEFAULT_LOAD_CONTEXT_STRATEGY = LoadStrategy.ALL;

    /**
     * 默认程序欢迎信息策略[HTML]
     * 详细信息参照 @WelcomeStrategy
     */
    public static final WelcomeStrategy DEFAULT_WELCOME_TYPE_STRATEGY = WelcomeStrategy.HTML;

    /**
     * URI[根路径]
     */
    public static String URI_ROOT = "/";

    /**
     * URI[/servlet]
     */
    public static String URI_SERVLET = "/servlet";

    /**
     * 文件夹名称[ROOT文件夹]
     */
    public static final String FOLDER_ROOT_NAME = "ROOT";

    /**
     * 文件名称[server.xml]
     */
    public static final String SERVER_XML_NAME = "server.xml";

    /**
     * 文件名称[web.xml]
     */
    public static final String WEB_XML_NAME = "web.xml";

    /**
     * 文件名称[content.xml]
     */
    public static final String CONTENT_XML_NAME = "content.xml";

    /**
     * 页面[timeConsume.html]
     */
    public static final String PAGE_NAME_HTML_TIME_CONSUME = "timeConsume.html";

    /**
     * 页面[exception.html]
     */
    public static final String PAGE_NAME_HTML_EXCEPTION = "exception.html";

    /**
     * 页面[index.html]
     */
    public static final String PAGE_NAME_HTML_INDEX = "index.html";

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
     * 信息[成功加载mimeType映射]
     */
    public static final String MSG_LOAD_MIME_TYPE_MAPPING = "Successfully loaded mime Type mappings: {}";

    /**
     * 信息[加载Web应用个数,从webApps目录下]
     */
    public static final String MSG_LOAD_APP_CONTEXT_COUNT_WEB_APP_FOLDER = "load application {} into context from \"webApps\" folder";

    /**
     * 信息[加载Web应用个数,从server.xml配置中]
     */
    public static final String MSG_LOAD_APP_CONTEXT_COUNT_SERVER_XML = "load application {} into context from \"server.xml\" config";

    /**
     * 信息[Scorpio初始化成功]
     */
    public static final String MSG_SCORPIO_INITIALIZE = "Scorpio initialize successfully, listening on port [http-bio-{}]";

    /**
     * 信息[Scorpio所有连接器初始化耗时]
     */
    public static final String MSG_CONNECTOR_INITIALIZE_TIME = "All Scorpio connectors take total time to initialize: {} millisecond.";

    /**
     * 信息[Scorpio启动成功]
     */
    public static final String MSG_SCORPIO_STARTED = "Scorpio started successfully, listening on port [http-bio-{}]";

    /**
     * 信息[修正Uri]
     */
    public static final String MSG_FIX_URI_APP = "URI correction successful [{}] -> [{}]";

    /**
     * 信息[ROOT程序不修正Uri]
     */
    public static final String MSG_FIX_URI_ROOT_APP = "Access the ROOT app without corrections";

    /**
     * 符号[\t]
     */
    public static final String SYMBOL_TAB = "\t";

    /**
     * 符号[\t\t]
     */
    public static final String SYMBOL_TAB_TAB = "\t\t";

    /**
     * 符号[\r\n]
     */
    public static final String SYMBOL_R_N = "\r\n";

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
    public final static File CONFIG_FOLDER = new File(SystemUtil.get("user.dir"), "config");

    /**
     * 配置文件[server.xml]
     */
    public final static File SERVER_XML_FILE = new File(CONFIG_FOLDER, SERVER_XML_NAME);

    /**
     * 配置文件[web.xml]
     */
    public final static File WEB_XML_FILE = new File(CONFIG_FOLDER, WEB_XML_NAME);

    /**
     * 配置文件[content.xml]
     */
    public final static File CONTENT_XML_FILE = new File(CONFIG_FOLDER, CONTENT_XML_NAME);

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

























