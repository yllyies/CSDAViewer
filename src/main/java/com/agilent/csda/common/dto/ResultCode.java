package com.agilent.csda.common.dto;

/**
 * 枚举了一些常用API操作码
 * Created by macro on 2019/4/19.
 */
public enum ResultCode implements IErrorCode {
    SUCCESS(200, "success"),
    FAILED(500, "failure"),
    VALIDATE_FAILED(404, "page not found"),
    UNAUTHORIZED(401, "unauthorized"),
    FORBIDDEN(403, "forbidden");
    private long code;
    private String message;

    private ResultCode(long code, String message) {
        this.code = code;
        this.message = message;
    }

    public long getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
