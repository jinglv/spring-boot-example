package com.example.mybatis.plus.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * Accessors(chain = true) 开启链式调用
 * <p>
 * 注意：如果不加对应的注解，则实体的表名和字段必须要与表的名称和列名必须一致
 *
 * @author jingLv
 * @date 2020/09/28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Accessors(chain = true)
public class User {
    private Long id;
    private String userName;
    private String passWord;
    private String gender;
    private String nickName;
}
