package com.example.mybatis.plus.dao;

import com.example.mybatis.plus.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author jingLv
 * @date 2020/09/28
 */
@SpringBootTest
class UserDaoTest {
    @Autowired
    private UserDao userDao;

    @Test
    void testFindAll() {
        List<User> users = userDao.selectList(null);
        users.forEach(System.out::println);
        users.forEach(user -> System.out.println("user=" + user));
    }

}