package com.example.excel.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author jingLv
 * @date 2021/01/26
 */
@Controller
public class IndexController {

    @RequestMapping("index")
    public String index() {
        return "index";
    }
}
