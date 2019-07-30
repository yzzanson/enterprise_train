package com.enterprise.base.exceptions;

/**
 * 业务异常
 *
 * @author shisan
 * @create 2017-09-14 上午11:13
 **/
public class BusinessException extends RuntimeException {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 表示信息
     */
    private String message;

    public BusinessException() {
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
