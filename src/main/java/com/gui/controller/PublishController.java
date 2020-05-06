package com.gui.controller;

import com.gui.Service.QuestionService;
import com.gui.cache.TagCache;
import com.gui.dto.QuestionDTO;
import com.gui.mapper.QuestionMapper;
import com.gui.mapper.UserMapper;
import com.gui.model.Question;
import com.gui.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by uuu on 2019/10/15.
 */
@Controller
public class PublishController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/publish/{id}")
    public  String edit(@PathVariable(name = "id")Integer id,Model model){
        QuestionDTO question=questionService.getById(id);
        model.addAttribute("title",question.getTitle());
        model.addAttribute("description",question.getDescription());
        model.addAttribute("tag",question.getTag());
        model.addAttribute("id",question.getId());//通过id来作为唯一标识，避免重复添加。
        //获取所有的标签
        model.addAttribute("tags", TagCache.get());
        return "publish";
    }


    @GetMapping("/publish")
    public String publish(Model model) {
        //获取所有的标签
        model.addAttribute("tags", TagCache.get());
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(@RequestParam(value = "title",required = false) String title,
                            @RequestParam(value = "description",required = false) String description,
                            @RequestParam(value = "tag",required = false) String tag,
                            @RequestParam(value = "id")Integer id,
                            HttpServletRequest request,Model model) {
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);
        //获取所有的标签
        model.addAttribute("tags", TagCache.get());
        if(title==null||title==""){
            model.addAttribute("error","标题不能为空");
            return "publish";
        }
        if(description==null||description==""){
            model.addAttribute("error","问题补充不能为空");
            return "publish";
        }
        if(tag==null||tag==""){
            model.addAttribute("error","标签不能为空");
            return "publish";
        }
        //传入当前输入的标签值
        String invalid=TagCache.filterInvalid(tag);
        if(StringUtils.isNotBlank(invalid)){
            model.addAttribute("error","输入非法标签:"+invalid);
            return "publish";
        }

        //获取user
        User user=(User)request.getSession().getAttribute("user");

        if(user==null){
            model.addAttribute("error","用户未登录");
            return "publish";
        }
        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setId(id);
        //判断id是否存在来更新或创建Question。
        questionService.CreateOrUpdate(question);
        return "redirect:/";
    }
}
