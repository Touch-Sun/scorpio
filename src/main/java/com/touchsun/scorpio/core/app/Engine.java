package com.touchsun.scorpio.core.app;

import cn.hutool.core.util.StrUtil;
import com.touchsun.scorpio.core.plugin.AppXMLParser;
import com.touchsun.scorpio.exception.ExceptionMessage;
import com.touchsun.scorpio.exception.ScorpioNormalException;
import lombok.Getter;

import java.util.List;

/**
 * Scorpio引擎
 * @author Lee
 */
public class Engine {

    /**
     * 属性[defaultHost]
     */
    private String defaultHost;

    /**
     * 子Host列表
     */
    private List<Host> hostList;

    /**
     * Scorpio引擎
     */
    @Getter
    private Service service;

    public Engine(Service service) throws ScorpioNormalException {
        // 从应用解析器解析[defaultHost]
        this.defaultHost = AppXMLParser.parseEngineDefaultHost();
        // 从应用解析器解析[hostList]
        this.hostList = AppXMLParser.parseHosts(this);
        // 赋值Service
        this.service = service;
        // 检测解析正确性
        this.checkParse();
    }

    public static Engine instance(Service service) throws ScorpioNormalException {
        return new Engine(service);
    }

    /**
     * 检测解析正确性
     * @throws ScorpioNormalException [内核异常]
     */
    private void checkParse() throws ScorpioNormalException {
        if (null == this.getDefaultHost()) {
            // 如果没有通过Scorpio引擎配置的信息找到Host,就抛出异常
            throw new ScorpioNormalException(StrUtil.format(ExceptionMessage.PARSE_ENGINE_EXCEPTION, defaultHost));
        }
    }

    /**
     * 根据Scorpio配置信息,获取Host
     * @return Host
     */
    public Host getDefaultHost() {
        for (Host host : this.hostList) {
            if (host.getName().equals(defaultHost)) {
                // 直接拿出localhost的Host
                return host;
            }
        }
        return null;
    }
}
