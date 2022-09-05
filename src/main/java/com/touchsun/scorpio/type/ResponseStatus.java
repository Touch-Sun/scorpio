package com.touchsun.scorpio.type;

import lombok.Getter;

/**
 * 响应状态码
 *
 * @author Lee
 */
public enum ResponseStatus {
    /**
     * 404
     */
    _404(404, "404 resource not found"),
    /**
     * 200
     */
    _200(200, "200 request succeeded"),
    /**
     * 500
     */
    _500(500, "500 internal server error"),
    /**
     * 302
     */
    _302(302, "302 URI temporary transfer");

    @Getter
    private final Integer code;

    private String message;

    ResponseStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
