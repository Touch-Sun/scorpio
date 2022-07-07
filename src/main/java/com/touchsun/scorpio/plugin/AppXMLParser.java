package com.touchsun.scorpio.plugin;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.LogFactory;
import com.touchsun.scorpio.config.ScorpioConfig;
import com.touchsun.scorpio.constant.ResponseConstant;
import com.touchsun.scorpio.core.*;
import com.touchsun.scorpio.exception.ScorpioNormalException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 应用解析器
 *
 * @author Lee
 */
public class AppXMLParser {

    /**
     * mime-type 映射类型
     */
    private static Map<String, String> mimeTypeMapping = new HashMap<>();

    /**
     * 解析配置文件,提取Context
     *
     * @return Context列表
     */
    public static List<Context> parseContexts() {
        List<Context> contextList = new ArrayList<>();
        // 读取出配置文件[server.xml]全部内容
        String serverXmlInfo = FileUtil.readUtf8String(ScorpioConfig.SERVER_XML_FILE);
        // 解析成Document
        Document document = Jsoup.parse(serverXmlInfo);
        // 获取Context节点
        Elements elements = document.select(ScorpioConfig.DEFAULT_SERVER_XML_ELEMENT_NAME_CONTEXT);
        // 遍历每一个Context,构造Context,存贮到contextList中
        for (Element element : elements) {
            String path = element.attr(ScorpioConfig.DEFAULT_SERVER_XML_ELEMENT_ATTRIBUTE_PATH_NAME);
            String appPath = element.attr(ScorpioConfig.DEFAULT_SERVER_XML_ELEMENT_ATTRIBUTE_APP_PATH_NAME);
            contextList.add(new Context(path, appPath));
        }
        return contextList;
    }

    /**
     * 解析配置文件,提取Host属性
     *
     * @return Host的属性name的值 [localhost]
     */
    public static String parseHostName() {
        // 读取出配置文件[server.xml]全部内容
        String serverXmlInfo = FileUtil.readUtf8String(ScorpioConfig.SERVER_XML_FILE);
        // 解析成Document
        Document document = Jsoup.parse(serverXmlInfo);
        // 获取Host节点
        Element host = document.select(ScorpioConfig.DEFAULT_SERVER_XML_ELEMENT_NAME_HOST).first();
        // 返回属性名称
        return host.attr(ScorpioConfig.DEFAULT_SERVER_XML_ELEMENT_ATTRIBUTE_NAME_NAME);
    }

    /**
     * 解析配置文件,提取Service属性
     *
     * @return Service的属性name的值 [Catalina]
     */
    public static String parseServiceName() {
        // 读取出配置文件[server.xml]全部内容
        String serverXmlInfo = FileUtil.readUtf8String(ScorpioConfig.SERVER_XML_FILE);
        // 解析成Document
        Document document = Jsoup.parse(serverXmlInfo);
        // 获取Host节点
        Element service = document.select(ScorpioConfig.DEFAULT_SERVER_XML_ELEMENT_NAME_SERVICE).first();
        // 返回属性名称
        return service.attr(ScorpioConfig.DEFAULT_SERVER_XML_ELEMENT_ATTRIBUTE_NAME_NAME);
    }

    /**
     * 解析配置文件,提取Engine属性
     *
     * @return Engine的属性defaultHost的值 [localhost]
     */
    public static String parseEngineDefaultHost() {
        // 读取出配置文件[server.xml]全部内容
        String serverXmlInfo = FileUtil.readUtf8String(ScorpioConfig.SERVER_XML_FILE);
        // 解析成Document
        Document document = Jsoup.parse(serverXmlInfo);
        // 获取Host节点
        Element engine = document.select(ScorpioConfig.DEFAULT_SERVER_XML_ELEMENT_NAME_ENGINE).first();
        // 返回属性名称
        return engine.attr(ScorpioConfig.DEFAULT_SERVER_XML_ELEMENT_ATTRIBUTE_DEFAULT_HOST_NAME);
    }

    /**
     * 在Engine节点下解析出Host节点列表[暂时仅支持一个Host]
     */
    public static List<Host> parseHosts(Engine engine) throws ScorpioNormalException {
        List<Host> hostList = new ArrayList<>();
        // 读取出配置文件[server.xml]全部内容
        String serverXmlInfo = FileUtil.readUtf8String(ScorpioConfig.SERVER_XML_FILE);
        // 解析成Document
        Document document = Jsoup.parse(serverXmlInfo);
        // 获取Host节点
        Elements elements = document.select(ScorpioConfig.DEFAULT_SERVER_XML_ELEMENT_NAME_HOST);
        // 遍历每一个Host,构造Host
        for (Element element : elements) {
            String name = element.attr(ScorpioConfig.DEFAULT_SERVER_XML_ELEMENT_ATTRIBUTE_NAME_NAME);
            hostList.add(new Host(name, engine));
        }
        return hostList;
    }

    /**
     * 在Service节点下解析出Connector节点列表
     */
    public static List<Connector> parseConnectors(Service service) {
        List<Connector> connectorList = new ArrayList<>();
        // 读取出配置文件[server.xml]全部内容
        String serverXmlInfo = FileUtil.readUtf8String(ScorpioConfig.SERVER_XML_FILE);
        // 解析成Document
        Document document = Jsoup.parse(serverXmlInfo);
        // 获取Host节点
        Elements elements = document.select(ScorpioConfig.DEFAULT_SERVER_XML_ELEMENT_NAME_CONNECTOR);
        // 遍历每一个Host,构造Host
        for (Element element : elements) {
            int port = Convert.toInt(element.attr(ScorpioConfig.DEFAULT_SERVER_XML_ELEMENT_ATTRIBUTE_PORT_NAME));
            Connector connector = Connector.instance(service);
            connector.setPort(port);
            connectorList.add(connector);
        }
        return connectorList;
    }

    /**
     * 获取某个应用上下文的默认欢迎文件的名称
     *
     * @param appContext 应用上下文
     * @return 该应用上下文中的欢迎文件
     */
    public static String parseWelcomeFile(Context appContext) {
        // 读取出配置文件[web.xml]全部内容
        String webXmlInfo = FileUtil.readUtf8String(ScorpioConfig.WEB_XML_FILE);
        // 解析成Document
        Document document = Jsoup.parse(webXmlInfo);
        // 获取welcome-file节点
        Elements elements = document.select(ScorpioConfig.DEFAULT_SERVER_XML_ELEMENT_NAME_WELCOME_FILE);
        // 遍历每一个welcome-file
        for (Element element : elements) {
            // 获取节点的文本信息
            String fileName = element.text();
            // 根据Context的应用路径,找到文件
            File welcomeFile = new File(appContext.getAppPath(), fileName);
            if (welcomeFile.exists()) {
                // 文件存在返回文件的名称
                return welcomeFile.getName();
            }
        }
        // 没有就返回默认[index.html]
        return ScorpioConfig.PAGE_NAME_HTML_INDEX;
    }

    /**
     * 根据extension获取mimeType
     * 该方法被设计为线程安全的,防止在并发情况下,造成判断失误,多次初始化mimeType映射
     *
     * @param extension extension
     * @return mimeType
     */
    public static synchronized String getMimeType(String extension) {
        if (mimeTypeMapping.isEmpty()) {
            // 若mimeType映射没有初始化
            initMimeTypeMapping();
            LogFactory.get().debug(ScorpioConfig.MSG_LOAD_MIME_TYPE_MAPPING, mimeTypeMapping.size());
        }
        // 获取mimeType
        String mimeType = mimeTypeMapping.get(extension);
        if (StrUtil.isEmpty(mimeType)) {
            // 没有获取到对应的mimeType,使用默认的[text/html]
            mimeType = ResponseConstant.TEXT_HTML;
        }
        return mimeType;
    }

    /**
     * 初始化mimeType映射
     */
    private static void initMimeTypeMapping() {
        // 读取出配置文件[web.xml]全部内容
        String webXmlInfo = FileUtil.readUtf8String(ScorpioConfig.WEB_XML_FILE);
        // 解析成Document
        Document document = Jsoup.parse(webXmlInfo);
        // 获取welcome-file节点
        Elements elements = document.select(ScorpioConfig.DEFAULT_SERVER_XML_ELEMENT_NAME_MIME_MAPPING);
        // 遍历每一个welcome-file
        for (Element element : elements) {
            // 获取extension节点
            String extension = element.select(ScorpioConfig.DEFAULT_SERVER_XML_ELEMENT_NAME_EXTENSION).first().text();
            // 获取mime-type节点
            String mimeType = element.select(ScorpioConfig.DEFAULT_SERVER_XML_ELEMENT_NAME_MIMETYPE).first().text();
            // 压入mimeType映射
            mimeTypeMapping.put(extension, mimeType);
        }
    }

}




















