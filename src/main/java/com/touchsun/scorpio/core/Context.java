package com.touchsun.scorpio.core;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.LogFactory;
import com.touchsun.scorpio.config.ScorpioConfig;
import com.touchsun.scorpio.exception.WebConfigDuplicatedException;
import com.touchsun.scorpio.plugin.ServletXMLParser;
import lombok.Getter;
import lombok.Setter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.*;

/**
 * 应用上下文
 *
 * @author Lee
 */
public class Context {

    /**
     * 应用访问路径
     */
    @Getter
    @Setter
    private String path;

    /**
     * 应用在文件系统中的位置
     */
    @Getter
    @Setter
    private String appPath;

    /**
     * web.xml配置文件
     * 此处对应为: XXX/WEB-INF/web.xml
     */
    private File contextXmlFile;

    /**
     * Servlet映射
     * 地址对应 Servlet 的类名
     */
    private Map<String, String> urlToServletClassName;

    /**
     * Servlet映射
     * 地址对应 Servlet 的名称
     */
    private Map<String, String> urlToServletName;

    /**
     * Servlet映射
     * Servlet 的名称对应类名
     */
    private Map<String, String> servletNameToClassName;

    /**
     * Servlet映射
     * Servlet 类名对应名称
     */
    private Map<String, String> classNameToServletName;

    public Context() {

    }

    public Context(String path, String appPath) {
        // 初始化应用路径配置
        this.path = path;
        this.appPath = appPath;
        // 初始化配置文件
        this.contextXmlFile = new File(this.appPath, ServletXMLParser.parseWatchedResource());
        // 初始化所有的缓存Map
        this.urlToServletClassName = new HashMap<>();
        this.urlToServletName = new HashMap<>();
        this.servletNameToClassName = new HashMap<>();
        this.classNameToServletName = new HashMap<>();
        // 开始部署
        this.deploy();
    }

    /**
     * 解析Servlet的XML映射信息
     *
     * @param document 文档
     */
    private void parseServletMapping(Document document) {
        // 解析URL -> ServletName映射
        // 获取servlet-mapping下的url-pattern节点
        Elements mappingElements = document.select(ScorpioConfig.DEFAULT_WEB_XML_ELEMENT_NAME_SERVLET_MAPPING +
                ScorpioConfig.SYMBOL_SPACE + ScorpioConfig.DEFAULT_WEB_XML_ELEMENT_NAME_URL_PATTERN);
        // 填充[urlToServletName]Map
        for (Element element : mappingElements) {
            // 得到[url-pattern]内容
            String urlPattern = element.text();
            // 得到[servlet-name]内容
            String servletName = element.parent()
                    .select(ScorpioConfig.DEFAULT_WEB_XML_ELEMENT_NAME_SERVLET_NAME).first().text();
            // 存储到Map
            this.urlToServletName.put(urlPattern, servletName);
        }
        // 解析servletName -> ClassName/className -> ServletName映射
        // 获取servlet下的servlet-name节点
        Elements nameElements = document.select(ScorpioConfig.DEFAULT_WEB_XML_ELEMENT_NAME_SERVLET +
                ScorpioConfig.SYMBOL_SPACE + ScorpioConfig.DEFAULT_WEB_XML_ELEMENT_NAME_SERVLET_NAME);
        // 填充[servletNameToClassName/classNameToServletName]映射
        for (Element nameElement : nameElements) {
            // 获取ServletName节点信息
            String servletName = nameElement.text();
            // 获取ServletClass节点信息
            String servletClass = nameElement.parent()
                    .select(ScorpioConfig.DEFAULT_WEB_XML_ELEMENT_NAME_SERVLET_CLASS).first().text();
            // 填充Map
            this.servletNameToClassName.put(servletName, servletClass);
            this.classNameToServletName.put(servletClass, servletName);
        }
        // 解析URL -> ServletClassName映射
        // 获取所有URL
        Set<String> urls = this.urlToServletName.keySet();
        // 填充[urlToServletClassName]Map
        for (String url : urls) {
            String servletName = this.urlToServletName.get(url);
            String servletClassName = this.servletNameToClassName.get(servletName);
            this.urlToServletClassName.put(url, servletClassName);
        }
    }

    /**
     * 检测映射集合是否有重复
     *
     * @param document 文档
     * @param node 节点
     * @param desc 重复原因描述
     * @throws WebConfigDuplicatedException 重复配置异常
     */
    private void checkDuplicated(Document document, String node, String desc) throws WebConfigDuplicatedException {
        // 查找[node]元素节点下的所有节点
        Elements elements = document.select(node);
        // 所有元素内容放入集合中
        List<String> content = new ArrayList<>();
        for (Element element : elements) {
            content.add(element.text());
        }
        // 列表排序
        Collections.sort(content);
        // 判断相邻的元素是否有重复
        for (int i = 0; i < content.size(); i++) {
            // 前节点
            String prev = content.get(i);
            // 后节点
            String next = content.get(i + 1);
            // 判断
            if (prev.equals(next)) {
                // 前节点内容等于后节点内容
                // 抛出重复配置异常
                throw new WebConfigDuplicatedException(StrUtil.format(desc, prev));
            }
        }
    }

    /**
     * 判断配置是否有重复
     *
     * @throws WebConfigDuplicatedException 配置重复异常
     */
    private void checkDuplicated() throws WebConfigDuplicatedException {
        // 获得Servlet配置文件信息
        String servletXmlInfo = FileUtil.readUtf8String(ScorpioConfig.CONTENT_XML_FILE);
        // 解析出Document文档
        Document document = Jsoup.parse(servletXmlInfo);
        // 判断servlet-mapping下的url-pattern是否重复
        this.checkDuplicated(document, ScorpioConfig.DEFAULT_WEB_XML_ELEMENT_NAME_SERVLET_MAPPING +
                ScorpioConfig.SYMBOL_SPACE + ScorpioConfig.DEFAULT_WEB_XML_ELEMENT_NAME_URL_PATTERN,
                ScorpioConfig.MSG_SERVLET_URL_REPEAT);
        // 判断servlet下的servlet-name是否重复
        this.checkDuplicated(document, ScorpioConfig.DEFAULT_WEB_XML_ELEMENT_NAME_SERVLET +
                ScorpioConfig.SYMBOL_SPACE + ScorpioConfig.DEFAULT_WEB_XML_ELEMENT_NAME_SERVLET_NAME,
                ScorpioConfig.MSG_SERVLET_NAME_REPEAT);
        // 判断servlet下的servlet-class是否重复
        this.checkDuplicated(document, ScorpioConfig.DEFAULT_WEB_XML_ELEMENT_NAME_SERVLET +
                ScorpioConfig.SYMBOL_SPACE + ScorpioConfig.DEFAULT_WEB_XML_ELEMENT_NAME_SERVLET_CLASS,
                ScorpioConfig.MSG_SERVLET_CLASS_REPEAT);
    }

    /**
     * 通过URI获取Servlet的类名
     *
     * @param uri URO
     * @return servlet的类名
     */
    public String getServletClassName(String uri) {
        return this.urlToServletClassName.get(uri);
    }

    /**
     * 上下文初始化
     */
    private void init() {
        if (!this.contextXmlFile.exists()) {
            // 配置文件不存在,终止初始化
            return;
        }
        // 先检测配置文件的正确性
        try {
            this.checkDuplicated();
        } catch (WebConfigDuplicatedException e) {
            e.printStackTrace();
            LogFactory.get().error(e);
            // 终止初始化
            return;
        }
        // 解析配置文件到Map中
        String contentXmlInfo = FileUtil.readUtf8String(this.contextXmlFile);
        Document document = Jsoup.parse(contentXmlInfo);
        this.parseServletMapping(document);
    }

    /**
     * 部署上下文
     */
    private void deploy() {
        // 计算部署用时
        TimeInterval timeInterval = DateUtil.timer();
        // 打印部署位置
        LogFactory.get().debug(ScorpioConfig.MSG_APP_DEPLOY_DIR, this.appPath);
        // 初始化
        this.init();
        // 打印部署用时日志
        LogFactory.get().debug(ScorpioConfig.MSG_DEPLOY_FINISH, this.appPath,
                ScorpioConfig.SCORPIO_PROTOCOL, ScorpioConfig.DEFAULT_ADDRESS,
                ScorpioConfig.DEFAULT_PORT, this.path, timeInterval.intervalMs());
    }

}




















