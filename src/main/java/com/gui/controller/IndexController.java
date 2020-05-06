package com.gui.controller;

import com.gui.Service.QuestionService;
import com.gui.dto.PaginationDTO;
import com.gui.dto.QuestionDTO;
import com.gui.mapper.QuestionMapper;
import com.gui.mapper.UserMapper;
import com.gui.model.Question;
import com.gui.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by uuu on 2019/8/29.
 */
@Controller
public class IndexController {
    @Resource
    private QuestionService questionService;
    @Resource
    private UserMapper userMapper;
    @GetMapping("/")
    /*利用search来完成主页的搜索功能*/
  public  String index(HttpServletRequest request,Model model,
                       @RequestParam(name = "page",defaultValue = "1")Integer page,
                       @RequestParam(name = "size",defaultValue = "5")Integer size,
                       @RequestParam(name = "search",required = false)String search)
    {

    //来加载question列表
      PaginationDTO pagination=questionService.list(search,page,size);
       model.addAttribute("pagination",pagination);
       model.addAttribute("search",search);
        return  "index";
    }
}
