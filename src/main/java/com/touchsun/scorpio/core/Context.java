package com.touchsun.scorpio.core;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.log.LogFactory;
import com.touchsun.scorpio.config.ScorpioConfig;
import lombok.Getter;
import lombok.Setter;

/**
 * 应用上下文
 *
 * @author Lee
 */
public class Context {

    /**
     * 应用访问路径
     */
    @Getter
    @Setter
    private String path;

    /**
     * 应用在文件系统中的位置
     */
    @Getter
    @Setter
    private String appPath;

    public Context() {

    }

    public Context(String path, String appPath) {
        // 计算加载所有应用耗费的时间
        TimeInterval timeInterval = DateUtil.timer();
        this.path = path;
        this.appPath = appPath;
        long deployConsume = timeInterval.interval();
        LogFactory.get().debug(ScorpioConfig.MSG_DEPLOY_FINISH, this.appPath,
                ScorpioConfig.SCORPIO_PROTOCOL, ScorpioConfig.DEFAULT_ADDRESS,
                ScorpioConfig.DEFAULT_PORT, this.path, deployConsume);
    }
}




















