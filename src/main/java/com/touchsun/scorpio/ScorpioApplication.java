package com.touchsun.scorpio;

import cn.hutool.log.LogFactory;
import com.touchsun.scorpio.core.Core;
import com.touchsun.scorpio.core.Server;
import com.touchsun.scorpio.exception.ScorpioNormalException;

/**
 * Scorpio静态启动类
 * @author Lee
 */
public class ScorpioApplication {

    /**
     * Scorpio App 启动方法
     * @param bootStrapClass 启动类
     * @param args 命令行参数
     */
    public static void run(Class<?> bootStrapClass, String[] args) {
        try {
            // 实例化内核
            Core core = Core.instance();
            // 实例化Scorpio服务器
            Server server = Server.instance(core);
            // 启动Scorpio服务器
            server.start();
        } catch (ScorpioNormalException e) {
            e.printStackTrace();
            LogFactory.get().error(e);
        }
    }

    public static void main(String[] args) {
        run(ScorpioApplication.class, args);
    }
}
