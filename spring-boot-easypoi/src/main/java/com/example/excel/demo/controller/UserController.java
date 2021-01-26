package com.example.excel.demo.controller;

import com.example.excel.demo.entity.User;
import com.example.excel.demo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author jingLv
 * @date 2021/01/26
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 查询所有
     *
     * @param request HttpServletRequest
     * @return 返回页面
     */
    @RequestMapping("/list")
    public String findAll(HttpServletRequest request) {
        List<User> users = userService.findAll();
        request.setAttribute("users", users);
        return "index";
    }
}
