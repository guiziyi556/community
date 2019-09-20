package com.gui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by uuu on 2019/8/29.
 */
@Controller
public class IndexController {
    @GetMapping("/")
  public  String index(){
        return  "index";
    }
}
