package com.example.jwt.dao;

import com.example.jwt.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author jingLv
 * @date 2020/09/30
 */
@SpringBootTest
class UserDaoTest {
    @Autowired
    private UserDAO userDao;

    @Test
    void login() {
        User user = new User();
        user.setUserName("admin");
        user.setPassWord("admin");
        User loginUser = userDao.login(user);
        System.out.println(loginUser);
    }

    @Test
    void getUsers() {
        List<User> users = userDao.getUsers();
        users.forEach(System.out::println);
    }
}