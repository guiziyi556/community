package com.gui.controller;

import com.gui.Service.CommentService;
import com.gui.dto.CommentCreateDTO;
import com.gui.dto.CommentDTO;
import com.gui.dto.ResultDTO;
import com.gui.enums.CommentTypeEnum;
import com.gui.exception.CustomizeErrorCode;
import com.gui.model.Comment;
import com.gui.model.User;
import com.gui.model.UserExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by uuu on 2020/2/10.
 */
@Controller
public class CommentController {

     @Resource
     private CommentService commentService;
     @ResponseBody
     @RequestMapping(value = "/comment",method = RequestMethod.POST)
    public  Object post(@RequestBody CommentCreateDTO commentCreateDTO, HttpServletRequest request){
         //在问题回复页面，判断是否登录用户，若未登录，提示异常
         User user=(User)request.getSession().getAttribute("user");
         if(user==null){
            // return ResultDTO.errorOf(2002,"未登录不能进行评论，请先登录");
              return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
         }
         //检验输入的评论内容是否为空，异常处理
          if(commentCreateDTO==null|| StringUtils.isBlank(commentCreateDTO.getContent())){
              return ResultDTO.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
          }
         Comment comment=new Comment();
         comment.setParentId(commentCreateDTO.getParentId());
         comment.setContent(commentCreateDTO.getContent());
         comment.setType(commentCreateDTO.getType());
         comment.setGmtModified(System.currentTimeMillis());
         comment.setGmtCreate(System.currentTimeMillis());
         comment.setCommentator(user.getId());
         //获取该评论的创建者
         commentService.insert(comment,user);
         /*return  null;*/
         /*Map<Object,Object> resultMap=new HashMap<>();
         resultMap.put("message","成功");*/
         //return resultMap;
          return ResultDTO.okOf();
     }

     //二级评论,二级评论的list。
    @ResponseBody
    @RequestMapping(value = "/comment/{id}",method = RequestMethod.GET)
    public  ResultDTO<List<CommentDTO>> comments(@PathVariable(name="id")Integer id){
    List<CommentDTO> commentDTOS =  commentService.listByTargetId(id, CommentTypeEnum.COMMENT);
        return ResultDTO.okOf(commentDTOS);
    }
}
