package com.example.mybatis.model;

import com.example.mybatis.enums.GenderEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author jingLv
 * @date 2020/09/24
 */
@Data
@NoArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 8162204514528429003L;

    /**
     * 表主键id
     */
    private Long id;
    /**
     * 用户姓名
     */
    private String userName;
    /**
     * 用户密码
     */
    private String passWord;
    /**
     * 用户性别
     */
    private GenderEnum gender;
    /**
     * 用户昵称
     */
    private String nickName;

    public User(String userName, String passWord, GenderEnum gender, String nickName) {
        this.userName = userName;
        this.passWord = passWord;
        this.gender = gender;
        this.nickName = nickName;
    }
}