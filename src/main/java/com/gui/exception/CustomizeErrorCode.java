package com.gui.exception;

/**
 * Created by uuu on 2020/1/29.
 */
public enum CustomizeErrorCode implements  ICustomizeErrorCode {

    QUESTION_NOT_FOUND(2001,"你找到的问题不存在，要不换一个试试？"),
    TAGET_PARAM_NOT_FOUND(2002,"未选中任何问题或评论进行回复"),
    NO_LOGIN(2003,"当前操作需要登录，请登录后重试"),
    SYS_ERROR(2004,"服务器冒烟了，请稍后再试"),
    REQUEST_ERROR(2005,"你这个请求错了吧，要不换一个"),
    TYPE_PARAM_WRONG(2006,"评论类型错误或不存在"),
    COMMENT_NOT_FOUND(2007,"回复的评论不存在，要不换个试试"),
    CONTENT_IS_EMPTY(2008,"输入的内容不能为空"),
    READ_NOTIFICATION_FAIL(2009,"不能读取他人回复信息"),
    NOTIFICATION_NOT_FOUND(2010,"消息莫非是不翼而飞了？"),
    ;
    @Override
    public  String getMessage(){
        return message;
    }
    @Override
    public Integer getCode(){return code;}
    private String message;
    private  Integer code;
    CustomizeErrorCode(Integer code,String message){
        this.message=message;
        this.code=code;
    }

}
