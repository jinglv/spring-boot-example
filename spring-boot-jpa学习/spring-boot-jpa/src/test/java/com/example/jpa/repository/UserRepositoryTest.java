package com.example.jpa.repository;

import com.example.jpa.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.DateFormat;
import java.util.Date;

/**
 * @author jingLv
 * @date 2021/01/18
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void test() {
        Date date = new Date();
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);
        String formattedDate = dateFormat.format(date);

        userRepository.save(new User("aa", "aa@126.com", "aa", "aa123456", formattedDate));
        userRepository.save(new User("bb", "bb@126.com", "bb", "bb123456", formattedDate));
        userRepository.save(new User("cc", "cc@126.com", "cc", "cc123456", formattedDate));

        System.out.println(userRepository.findAll().size());
    }
}