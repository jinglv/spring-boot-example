package com.example.mybatis.multi;

import com.example.mybatis.multi.mapper.one.UserOneMapper;
import com.example.mybatis.multi.mapper.two.UserTwoMapper;
import com.example.mybatis.multi.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

/**
 * @author jingLv
 * @date 2021/01/15
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserMapperTest {
    @Autowired
    private UserOneMapper userOneMapper;
    @Autowired
    private UserTwoMapper userTwoMapper;

    @Test
    void getUser() {
        List<User> oneUsers = userOneMapper.getAllUsers();
        List<User> twoUsers = userTwoMapper.getAllUsers();
        oneUsers.forEach(System.out::println);
        twoUsers.forEach(System.out::println);
    }
}
