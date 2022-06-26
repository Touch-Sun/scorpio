package com.touchsun.scorpio.core;

import com.touchsun.scorpio.plugin.AppXMLParser;
import com.touchsun.scorpio.exception.ScorpioNormalException;
import lombok.Getter;

/**
 * Scorpio服务
 * @author Lee
 */
public class Service {

    /**
     * 属性[name]
     */
    private String name;

    /**
     * 子Engine引擎
     */
    @Getter
    private Engine engine;

    /**
     * Scorpio服务器
     */
    private Server server;

    public Service(Server server) throws ScorpioNormalException {
        // 从应用解析器解析[name]
        this.name = AppXMLParser.parseServiceName();
        // 实例化一个Engine引擎
        this.engine = Engine.instance(this);
        // 赋值Scorpio服务器
        this.server = server;
    }

    public static Service instance(Server server) throws ScorpioNormalException {
        return new Service(server);
    }
}
