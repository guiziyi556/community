package com.gui.dto;

import com.gui.exception.CustomizeErrorCode;
import com.gui.exception.CustomizeException;
import lombok.Data;

/**
 * Created by uuu on 2020/2/17.
 */
@Data
public class ResultDTO<T> {
    private Integer code;
    private String message;
    private T data;

    //类构造方法，方便使用
    public ResultDTO(Integer code, String message) {
        //ResultDTO resultDTO=new ResultDTO();
        this.code = code;
        this.message = message;
    }
    public  ResultDTO(){

    }


    public static ResultDTO errorOf(CustomizeErrorCode exception) {
        return new ResultDTO(exception.getCode(), exception.getMessage());
    }

    public static ResultDTO errorOf(CustomizeException exception) {
        return new ResultDTO(exception.getCode(), exception.getMessage());
    }

    //成功信息
    public static ResultDTO okOf() {
        return new ResultDTO(200, "请求成功");
    }

    //返回成功信息的同时，返回data信息
    public static <T>ResultDTO okOf(T t) {
          ResultDTO resultDTO=new ResultDTO();
          resultDTO.setMessage("请求成功");
          resultDTO.setCode(200);
          resultDTO.setData(t);
          return resultDTO;
    }
}
