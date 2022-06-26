package com.touchsun.scorpio.type;

/**
 * 程序加载策略
 * @author Lee
 */
public enum LoadStrategy {
    // 仅加载[webApps]目录下的应用
    APP,
    // 仅加载[server.xml]配置的应用
    XML,
    // 同时加载
    ALL
}
