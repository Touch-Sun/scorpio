package com.touchsun.scorpio.core;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.log.LogFactory;
import com.touchsun.scorpio.config.ScorpioConfig;
import com.touchsun.scorpio.plugin.AppXMLParser;
import com.touchsun.scorpio.exception.ScorpioNormalException;
import lombok.Getter;

import java.util.List;

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
     * Scorpio内核
     */
    @Getter
    private Core core;

    /**
     * Scorpio连接器列表
     */
    private List<Connector> connectorList;

    /**
     * Scorpio服务器
     */
    private Server server;

    public Service(Server server, Core core) throws ScorpioNormalException {
        // 从应用解析器解析[name]
        this.name = AppXMLParser.parseServiceName();
        // 实例化一个Engine引擎
        this.engine = Engine.instance(this);
        // 赋值Scorpio服务器
        this.server = server;
        // 赋值Scorpio内核
        this.core = core;
        // 从应用解析器解析[Connectors]
        this.connectorList = AppXMLParser.parseConnectors(this);
    }

    public static Service instance(Server server, Core core) throws ScorpioNormalException {
        return new Service(server, core);
    }

    /**
     * Scorpio服务启动
     */
    public void start() {
        this.initialize();
    }

    /**
     * Scorpio服务初始化
     */
    public void initialize() {
        // 计算初始化Connector耗时
        TimeInterval timeInterval = DateUtil.timer();
        // 初始化每一个连接器
        connectorList.forEach(Connector::initialize);
        LogFactory.get().info(ScorpioConfig.MSG_CONNECTOR_INITIALIZE_TIME, timeInterval.interval());
        // 将每一个连接器提交到线程池
        connectorList.forEach(Connector::start);
    }
}
