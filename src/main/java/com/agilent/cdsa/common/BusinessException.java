package com.agilent.cdsa.common;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 业务异常类, 使用场景:程序并未出现执行异常情况,人为抛出异常信息。
 * 例如：登录功能,账号不存在或密码错误时,可抛出一个业务异常,自定义异常信息
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 异常对应的返回码
     */
    private Integer code;

    /**
     * 异常对应的描述信息
     */
    private String message;

    private Throwable throwable;

    public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        super(message);
        this.message = message;
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.message = String.format("%s %s", message, cause.getMessage());
    }

    public BusinessException(int code, String message, Throwable throwable) {
        super(message);
        this.code = code;
        this.message = message;
        this.throwable = throwable;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}