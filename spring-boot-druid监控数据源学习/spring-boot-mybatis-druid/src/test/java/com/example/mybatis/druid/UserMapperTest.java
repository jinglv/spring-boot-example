package com.example.mybatis.druid;


import com.example.mybatis.druid.entity.User;
import com.example.mybatis.druid.enums.GenderEnum;
import com.example.mybatis.druid.mapper.UserMapper;
import com.example.mybatis.druid.param.UserParam;
import com.example.mybatis.druid.result.Page;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jingLv
 * @date 2021/01/14
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserMapperTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    void getAllUsers() {
        List<User> users = userMapper.getAllUsers();
        users.forEach(System.out::println);
    }

    @Test
    void getOne() {
        User user = userMapper.getOne(1L);
        System.out.println(user);
    }

    @Test
    void getListUserByUserGender() {
        List<User> man = userMapper.getListUserByUserGender("MAN");
        man.forEach(System.out::println);
    }

    @Test
    void getListUserByNameAndGender() {
        Map<String, Object> map = new HashMap<>();
        map.put("userName", "admin");
        map.put("gender", "MAN");
        List<User> user = userMapper.getListUserByNameAndGender(map);
        user.forEach(System.out::println);
    }

    @Test
    void insertUser() {
        User user = new User();
        user.setUserName("huahua").setPassWord("123456").setGender(GenderEnum.WOMAN).setNickName("花妹");
        int i = userMapper.insertUser(user);
        System.out.println(i);
    }

    @Test
    void getList() {
        UserParam userParam = new UserParam();
        userParam.setGender("WOMAN");
        userParam.setCurrentPage(1);

        List<User> users = userMapper.getList(userParam);
        long count = userMapper.getCount(userParam);
        Page<User> page = new Page<>(userParam, count, users);
        System.out.println(page);
    }
}