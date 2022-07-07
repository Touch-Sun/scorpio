package com.touchsun.scorpio.exception;

/**
 * 异常提示信息
 *
 * @author Lee
 */
public class ExceptionMessage {

    /**
     * 没有指定Web应用的加载策略 [内核异常]
     */
    public static final String LOAD_STRATEGY_EXCEPTION = "Does not have a default program loading strategy!";

    /**
     * 没有指定Web应用的欢迎策略 [内核异常]
     */
    public static final String WELCOME_STRATEGY_EXCEPTION = "Does not have a default program welcome strategy!";

    /**
     * 响应状态不存在 [内核异常]
     */
    public static final String RESPONSE_STATUS_NOT_EXIST_EXCEPTION = "Response status does not exist!";

    /**
     * Scorpio引擎节点的配置有误 [内核异常]
     */
    public static final String PARSE_ENGINE_EXCEPTION = "Could not resolve Scorpio engine ENGINE because default Host[{}] does not exist!";

    /**
     * 主动创建了一个异常 [自定义触发异常]
     */
    public static final String CREATED_EXCEPTION = "Actively created an exception!";
}
