package com.example.shiro.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author jinglv
 * @date 2020/10/04
 */
@Controller
@RequestMapping("user")
public class UserController {

    /**
     * 处理身份认证
     *
     * @param username 用户名
     * @param password 密码
     * @return 返回认证信息
     */
    @RequestMapping("login")
    public String login(String username, String password) {
        // 获取主体对象
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(new UsernamePasswordToken(username, password));
            return "redirect:/index.jsp";
        } catch (IncorrectCredentialsException e) {
            e.printStackTrace();
            System.out.println("密码错误！");
        } catch (UnknownAccountException e) {
            e.printStackTrace();
            System.out.println("用户名错误！");
        }
        return "redirect:/login.jsp";
    }

    /**
     * 退出登录
     *
     * @return
     */
    @RequestMapping("logout")
    public String logout() {
        // 获取主体对象
        Subject subject = SecurityUtils.getSubject();
        // 退出操作
        subject.logout();
        return "redirect:/login.jsp";
    }
}
