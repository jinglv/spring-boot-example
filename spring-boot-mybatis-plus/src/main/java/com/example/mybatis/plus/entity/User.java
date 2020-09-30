package com.example.mybatis.plus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * Accessors(chain = true) 开启链式调用
 * <p>
 * 注意：如果不加对应的注解，则实体的表名和字段必须要与表的名称和列名必须一致
 * <p>
 * 默认将类名作为表名
 *
 * @author jingLv
 * @date 2020/09/28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Accessors(chain = true)
@TableName("users")
public class User {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 工号
     */
    @TableField("job_id")
    private Long jobId;
    /**
     * 用户名
     */
    @TableField("user_name")
    private String userName;
    /**
     * 用户密码
     */
    @TableField("pass_word")
    private String passWord;
    /**
     * 用户年龄
     */
    @TableField(condition = "%s&lt;#{%s}")
    private Integer age;
    /**
     * 用户邮箱
     */
    private String email;
    /**
     * 直属上级ID（上级工号）
     */
    @TableField("manager_id")
    private Long managerId;

    /**
     * 说明
     */
    @TableField(exist = false)
    private String description;
}
