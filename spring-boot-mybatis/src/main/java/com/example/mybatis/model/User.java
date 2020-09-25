package com.example.mybatis.model;

import com.example.mybatis.enums.GenderEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @author jingLv
 * @date 2020/09/24
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String userName;
    private String passWord;
    private GenderEnum gender;
    private String nickName;

    public User() {
    }

    public User(String userName, String passWord, GenderEnum gender, String nickName) {
        this.userName = userName;
        this.passWord = passWord;
        this.gender = gender;
        this.nickName = nickName;
    }
}