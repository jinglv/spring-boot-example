package com.example.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


/**
 * 根据实体配置会生成数据表
 *
 * @author jingLv
 * @date 2021/01/18
 */
@Entity
public class User {
    /**
     * 表主键id
     */
    @Id
    @GeneratedValue
    private Long id;
    /**
     * 用户名
     */
    @Column(nullable = false, unique = true)
    private String userName;
    /**
     * 密码
     */
    @Column(nullable = false)
    private String passWord;
    /**
     * 邮箱
     */
    @Column(nullable = false, unique = true)
    private String email;
    /**
     * 昵称
     */
    @Column(nullable = true, unique = true)
    private String nickName;
    /**
     * 注册时间
     */
    @Column(nullable = false)
    private String regTime;

    public User() {
    }

    public User(String userName, String passWord, String email, String nickName, String regTime) {
        this.userName = userName;
        this.passWord = passWord;
        this.email = email;
        this.nickName = nickName;
        this.regTime = regTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRegTime() {
        return regTime;
    }

    public void setRegTime(String regTime) {
        this.regTime = regTime;
    }
}
