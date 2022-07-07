package com.touchsun.scorpio.core;

import com.touchsun.scorpio.exception.ScorpioNormalException;

/**
 * Scorpio服务器
 *
 * @author Lee
 */
public class Server {

    /**
     * Scorpio服务
     */
    private Service service;

    /**
     * Scorpio内核
     */
    private Core core;

    public Server(Core core) throws ScorpioNormalException {
        this.service = Service.instance(this, core);
        this.core = core;
    }

    public static Server instance(Core core) throws ScorpioNormalException {
        return new Server(core);
    }

    /**
     * 启动Scorpio服务
     */
    public void start() {
        this.core.jvmLog();
        this.initialize();
    }

    /**
     * 初始化Scorpio服务
     */
    public void initialize() {
        this.service.start();
    }
}
