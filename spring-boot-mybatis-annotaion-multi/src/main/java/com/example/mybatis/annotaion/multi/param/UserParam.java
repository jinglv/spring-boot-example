package com.example.mybatis.annotaion.multi.param;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * User 的查询条件参数类继承分页基础类
 *
 * @author jingLv
 * @date 2020/09/27
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserParam extends PageParam {
    /**
     * 查询条件--用户姓名
     */
    private String userName;
    /**
     * 查询条件--用户性别
     */
    private String gender;
}
