package com.touchsun.scorpio.core.plugin;

import cn.hutool.core.io.FileUtil;
import com.touchsun.scorpio.core.app.Context;
import com.touchsun.scorpio.core.config.ScorpioConfig;
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
        Elements elements = document.select(ScorpioConfig.DEFAULT_SERVER_XML_ELEMENT_NAME);
        // 遍历每一个Context,构造Context,存贮到contextList中
        for (Element element : elements) {
            String path = element.attr(ScorpioConfig.DEFAULT_SERVER_XML_ELEMENT_ATTRIBUTE_PATH_NAME);
            String appPath = element.attr(ScorpioConfig.DEFAULT_SERVER_XML_ELEMENT_ATTRIBUTE_APP_PATH_NAME);
            contextList.add(new Context(path, appPath));
        }
        return contextList;
    }
}
