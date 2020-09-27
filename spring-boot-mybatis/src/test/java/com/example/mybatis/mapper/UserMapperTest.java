package com.example.mybatis.mapper;

import com.example.mybatis.enums.GenderEnum;
import com.example.mybatis.model.User;
import com.example.mybatis.param.UserParam;
import com.example.mybatis.result.Page;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author jingLv
 * @date 2020/09/24
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void getAllUsers() {
        List<User> users = userMapper.getAllUsers();
        users.forEach(System.out::println);
    }

    @Test
    public void getOne() {
        User user = userMapper.getOne(1L);
        System.out.println(user.toString());
    }

    @Test
    public void insertUser() {
        userMapper.insertUser(new User("咪咪", "123123", GenderEnum.WOMAN, "阿咪"));
    }

    @Test
    public void updateUser() {
        // 1.先查询用户信息
        User user = userMapper.getOne(1L);
        // 2. 修改用户
        user.setUserName("花花");
        // 3.调用修改
        userMapper.updateUser(user);
    }

    @Test
    public void deleteUser() {
        userMapper.deleteUser(1l);
    }

    @Test
    public void testPage() {
        UserParam userParam = new UserParam();
        userParam.setGender("WOMAN");
        userParam.setCurrentPage(1);

        List<User> users = userMapper.getList(userParam);
        long count = userMapper.getCount(userParam);
        Page page = new Page(userParam, count, users);
        System.out.println(page);
    }
}