package com.enterprise.base.exceptions;

/**
 * 业务异常
 *
 * @author shisan
 * @create 2017-09-14 上午11:13
 **/
public class LoginException extends RuntimeException {

    public LoginException() {
    }

    public LoginException(String message) {
        super(message);
    }

}
