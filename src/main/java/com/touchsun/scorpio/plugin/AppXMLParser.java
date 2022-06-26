package com.touchsun.scorpio.plugin;

import cn.hutool.core.io.FileUtil;
import com.touchsun.scorpio.core.Context;
import com.touchsun.scorpio.core.Engine;
import com.touchsun.scorpio.core.Host;
import com.touchsun.scorpio.config.ScorpioConfig;
import com.touchsun.scorpio.exception.ScorpioNormalException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * 应用解析器
 * @author Lee
 */
public class AppXMLParser {

    /**
     * 解析配置文件,提取Context
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

}




















