package com.gui.exception;

/**
 * Created by uuu on 2020/1/27.
 */
public class CustomizeException extends RuntimeException{
    private String message;
    private Integer code;

    public CustomizeException(ICustomizeErrorCode errorCode)
    {
         this.message=errorCode.getMessage();
         this.code=errorCode.getCode();
    }

    @Override
    public String getMessage() {
        return message;
    }
    public Integer getCode(){
        return code;
    }
}

