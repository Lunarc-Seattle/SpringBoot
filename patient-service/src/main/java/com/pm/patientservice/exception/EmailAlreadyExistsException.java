package com.pm.patientservice.exception;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String message) {
        super(message);
        //super(message) 是把错误信息传给父类，方便记录日志和调试。
    }
}