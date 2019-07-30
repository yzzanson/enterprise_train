package com.enterprise.mobile.web.exception;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/12/18 上午9:42
 */
public class OverloadException extends Exception {

    String message; //定义String类型变量

    public OverloadException(String ErrorMessagr) {  //父类方法
        message = ErrorMessagr;
    }

    public String getMessage() {   //覆盖getMessage()方法
        return message;
    }

}
