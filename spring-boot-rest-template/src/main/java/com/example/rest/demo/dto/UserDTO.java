package com.example.rest.demo.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 接口响应的的数据实体
 *
 * @author jingLv
 * @date 2021/02/02
 */
@Data
public class UserDTO implements Serializable {

    private static final long serialVersionUID = -6951716272821906781L;
    
    /**
     * 用户编号
     */
    private Integer userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;
}
