package com.touchsun.scorpio.plugin;

import cn.hutool.core.io.FileUtil;
import cn.hutool.log.LogFactory;
import com.touchsun.scorpio.config.ScorpioConfig;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Servlet配置解析器
 *
 * @author Lee
 */
public class ServletXMLParser {

    /**
     * 解析节点[WatchedResource]内容
     *
     * @return 元素内容
     */
    public static String parseWatchedResource() {
        try {
            // 读取XML信息
            String contentXmlInfo = FileUtil.readUtf8String(ScorpioConfig.CONTENT_XML_FILE);
            // 解析成Document
            Document document = Jsoup.parse(contentXmlInfo);
            // 查找[WatchedResource]节点
            Element element = document.select(ScorpioConfig.DEFAULT_CONTENT_XML_ELEMENT_NAME_WATCHEDRESOURCE).first();
            // 返回元素内容
            return element.text();
        } catch (Exception e) {
            e.printStackTrace();
            LogFactory.get().error(e);
            return ScorpioConfig.DEFAULT_WEB_XML_PATH;
        }
    }
}
