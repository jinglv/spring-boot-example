package com.example.jpa.repository;

import com.example.jpa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author jingLv
 * @date 2021/01/18
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * 通过用户名查询
     *
     * @param userName
     * @return
     */
    User findByUserName(String userName);

    /**
     * 通过用户名和邮箱查询
     *
     * @param username
     * @param email
     * @return
     */
    User findByUserNameOrEmail(String username, String email);
}
