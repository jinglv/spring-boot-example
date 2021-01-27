package com.example.excel.demo.service.impl;

import com.example.excel.demo.dao.UserDAO;
import com.example.excel.demo.entity.User;
import com.example.excel.demo.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author jingLv
 * @date 2021/01/26
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;

    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * 查询所有用户信息
     *
     * @return 返回用户信息列表
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<User> findAll() {
        return userDAO.findAll();
    }

    /**
     * 保存所有用户信息
     *
     * @param users 用户信息
     */
    @Override
    public void saveAll(List<User> users) {
        users.forEach(user -> {
            user.setId(null);
            String photoFileName = user.getPhoto().substring(user.getPhoto().lastIndexOf("/") + 1);
            user.setPhoto(photoFileName);
            userDAO.save(user);
        });
    }
}
