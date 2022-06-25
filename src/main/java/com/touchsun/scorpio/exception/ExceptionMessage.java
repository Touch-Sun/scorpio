package com.touchsun.scorpio.exception;

/**
 * 异常提示信息
 * @author Lee
 */
public class ExceptionMessage {

    /**
     * 没有指定Web应用的加载策略 [内核异常]
     */
    public static final String LOAD_STRATEGY_EXCEPTION = "Does not have a default program loading strategy!";

    /**
     * Scorpio引擎节点的配置有误 [内核异常]
     */
    public static final String PARSE_ENGINE_EXCEPTION = "Could not resolve Scorpio engine ENGINE because default Host[{}] does not exist!";
}
