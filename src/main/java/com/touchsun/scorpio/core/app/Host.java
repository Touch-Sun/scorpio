package com.touchsun.scorpio.core.app;

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.LogFactory;
import com.touchsun.scorpio.core.config.ScorpioConfig;
import com.touchsun.scorpio.core.plugin.AppXMLParser;
import com.touchsun.scorpio.exception.ExceptionMessage;
import com.touchsun.scorpio.exception.ScorpioNormalException;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 虚拟主机
 * @author Lee
 */
public class Host {

    /**
     * 属性[name]
     */
    @Getter
    @Setter
    private String name;

    /**
     * Scorpio引擎
     */
    private Engine engine;

    /**
     * 访问路径 -> Context
     */
    public Map<String, Context> appContext;

    public Host(String name, Engine engine) throws ScorpioNormalException {
        // 初始化[name][appContext]
        this.appContext = new HashMap<>();
        // 通过引擎解析来设置
        this.name = name;
        // 设置引擎
        this.engine = engine;
        // 扫描应用到上下文
        loadApplicationContext();
    }

    /**
     * 加载Web应用到上下文[webApps目录下]
     */
    public void loadApplicationContext() throws ScorpioNormalException {
        switch (ScorpioConfig.DEFAULT_LOAD_CONTEXT_STRATEGY) {
            case APP:
                loadApplicationContextFormWebAppsFolder();
                break;
            case XML:
                loadApplicationContextFromServerXml();
                break;
            case ALL:
                loadApplicationContextFormWebAppsFolder();
                loadApplicationContextFromServerXml();
                break;
            default:
                throw new ScorpioNormalException(ExceptionMessage.LOAD_STRATEGY_EXCEPTION);
        }
    }

    /**
     * 加载Web应用到上下文[webApps目录下]
     */
    public void loadApplicationContextFormWebAppsFolder() {
        // 获取WebApps下的所有文件/文件夹
        File[] files = ScorpioConfig.WEBAPPS_FOLDER.listFiles();
        int count = 0;
        assert files != null;
        for (File file : files) {
            // 目录代表是一个App
            if (file.isDirectory()) {
                String path = file.getName();
                if (StrUtil.equals(ScorpioConfig.FOLDER_ROOT_NAME, path)) {
                    // ROOT文件夹的访问路径默认为 "/"
                    path = ScorpioConfig.URI_ROOT;
                } else {
                    // App的访问路径 "/" + folder name -> "/numbers"
                    path = ScorpioConfig.URI_ROOT + path;
                }
                // 存储到上下文Map
                appContext.put(path, new Context(path, file.getAbsolutePath()));
                count++;
            }
        }
        LogFactory.get().debug(ScorpioConfig.MSG_LOAD_APP_CONTEXT_COUNT_WEB_APP_FOLDER, count);
    }

    /**
     * 加载Web应用到上下文[server.xml配置文件中]
     */
    public void loadApplicationContextFromServerXml() {
        AtomicInteger count = new AtomicInteger();
        AppXMLParser.parseContexts().forEach(context -> {
            appContext.put(context.getPath(), context);
            count.getAndIncrement();
        });
        LogFactory.get().debug(ScorpioConfig.MSG_LOAD_APP_CONTEXT_COUNT_SERVER_XML, count);
    }

    /**
     * 获取Context
     * @param path 访问路径
     * @return Context
     */
    public Context getContext(String path) {
        return appContext.get(path);
    }


}
