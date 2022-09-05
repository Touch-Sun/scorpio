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

    public Server() throws ScorpioNormalException {
        this.core = Core.instance();
        this.service = Service.instance(this, core);
    }

    public static Server instance() throws ScorpioNormalException {
        return new Server();
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
