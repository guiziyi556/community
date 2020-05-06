package com.gui.advice;

import com.alibaba.fastjson.JSON;
import com.gui.dto.ResultDTO;
import com.gui.exception.CustomizeErrorCode;
import com.gui.exception.CustomizeException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by uuu on 2020/1/27.
 */
@ControllerAdvice
public class CustomizeExceptionHandler {
    @ExceptionHandler(Exception.class)
    ModelAndView handle(HttpServletRequest request, Throwable e, Model model, HttpServletResponse response){
       //HttpStatus status=getStatus(request);
        String contentType=request.getContentType();
        if("application/json".equals(contentType)){
            ResultDTO resultDTO;
            if(e instanceof CustomizeException){
                resultDTO=ResultDTO.errorOf((CustomizeException)e);
            }else{
                resultDTO=ResultDTO.errorOf(CustomizeErrorCode.SYS_ERROR);
            }

            try {
                //利用response来返回值
                response.setContentType("application/json");
                response.setStatus(200);
                response.setCharacterEncoding("utf-8");
                PrintWriter writer=response.getWriter();
                writer.write(JSON.toJSONString(resultDTO));
                writer.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return null;
        }else{
            //返回到错误页面
            if(e instanceof CustomizeException){
                model.addAttribute("message",e.getMessage());
            }else {
                model.addAttribute("message", CustomizeErrorCode.SYS_ERROR.getMessage());
            }
            return new ModelAndView("error");
        }
    }
  /*  private HttpStatus getStatus (HttpServletRequest request){
        Integer statusCode=(Integer) request.getAttribute("javax.servlet.error.status_code");
        if(statusCode==null){
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }*/
}
