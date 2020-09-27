package com.example.mybatis.param;

import lombok.Data;

/**
 * User的分页类
 *
 * @author jingLv
 * @date 2020/09/27
 */
@Data
public class UserParam extends PageParam {
    private String userName;
    private String gender;
}
