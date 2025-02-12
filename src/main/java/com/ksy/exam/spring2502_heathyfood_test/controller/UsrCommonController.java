package com.ksy.exam.spring2502_heathyfood_test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/common")
public class UsrCommonController {

    @RequestMapping("/introduce")
    public String introduce(){
        return "usr/common/introduce";
    }

}
