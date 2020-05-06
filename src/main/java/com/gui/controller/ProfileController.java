package com.gui.controller;

import com.gui.Service.NotificationService;
import com.gui.Service.QuestionService;
import com.gui.dto.PaginationDTO;
import com.gui.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by uuu on 2019/12/5.
 */
@Controller
public class ProfileController {
    @Resource
    private QuestionService questionService;
    @Resource
    private NotificationService notificationService;

    @GetMapping("/profile/{action}")
    public  String profile(HttpServletRequest request, @PathVariable(name = "action")String action, Model model, @RequestParam(name = "page",defaultValue = "1")Integer page,
                           @RequestParam(name = "size",defaultValue = "5")Integer size){
        User user=(User)request.getSession().getAttribute("user");
        if(user==null){
            return "redirect:/";
        }

        if("questions".equals(action)){
            model.addAttribute("section","questions");
            model.addAttribute("sectionName","我的问题");
            //需要根据当前用户的id来显示问题列表
            PaginationDTO  pagination=questionService.list(user.getId(),page,size);
            model.addAttribute("pagination",pagination);
        }else if("replies".equals(action)){
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","最新回复");
            //显示最新回复信息
            PaginationDTO pagination=notificationService.list(user.getId(),page,size);
            //提供未读回复数
     /*       Long unreadCount=notificationService.unreadCount(user.getId());*/
            model.addAttribute("pagination",pagination);
          /*  model.addAttribute("unreadCount",unreadCount);*/
        }

        return "profile";
    }
}
