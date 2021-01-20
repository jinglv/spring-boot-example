package com.example.jwt.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author jingLv
 * @date 2020/09/30
 */
@Data
@Accessors(chain = true)
public class User {
    /**
     * 主键id
     */
    private String id;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 密码
     */
    private String passWord;
}
