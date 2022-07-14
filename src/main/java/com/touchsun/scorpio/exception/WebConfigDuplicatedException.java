package com.touchsun.scorpio.exception;

/**
 * Web.xml 配置重复异常
 *
 * @author Lee
 */
public class WebConfigDuplicatedException extends Exception {

    public WebConfigDuplicatedException(String message) {
        super(message);
    }
}
