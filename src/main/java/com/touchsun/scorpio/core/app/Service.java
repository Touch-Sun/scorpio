package com.touchsun.scorpio.core.app;

import com.touchsun.scorpio.core.plugin.AppXMLParser;
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

    public Service() throws ScorpioNormalException {
        // 从应用解析器解析[name]
        this.name = AppXMLParser.parseServiceName();
        // 实例化一个Engine引擎
        this.engine = Engine.instance(this);
    }

    public static Service instance() throws ScorpioNormalException {
        return new Service();
    }
}
