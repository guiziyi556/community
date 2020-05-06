package com.gui.controller;

import com.gui.Service.CommentService;
import com.gui.Service.QuestionService;
import com.gui.dto.CommentDTO;
import com.gui.dto.QuestionDTO;
import com.gui.enums.CommentTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Created by uuu on 2019/12/10.
 */
@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private CommentService commentService;
    @GetMapping("/question/{id}")
    public String question(@PathVariable(name="id")Integer id, Model model) throws  Exception{
        //加载问题详情
        QuestionDTO questionDTO=questionService.getById(id);

        //同时加载该问题下的所有评论
      List<CommentDTO> comments= commentService.listByTargetId(id, CommentTypeEnum.QUESTION);
       //加载相关问题
      List<QuestionDTO> relatedQuestions=  questionService.selectRelated(questionDTO);
        //阅读数更新，viewCount的增加
        questionService.incView(id);
        model.addAttribute("question",questionDTO);
        model.addAttribute("comments",comments);
        model.addAttribute("relatedQuestions",relatedQuestions);
        return "question";
    }
}
