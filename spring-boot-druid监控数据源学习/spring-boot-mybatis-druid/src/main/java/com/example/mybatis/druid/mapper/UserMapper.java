package com.example.mybatis.druid.mapper;


import com.example.mybatis.druid.entity.User;
import com.example.mybatis.druid.enums.GenderEnum;
import com.example.mybatis.druid.param.UserParam;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * @author jingLv
 * @date 2020/09/24
 */
public interface UserMapper {
    /**
     * 获取所有的用户数据
     *
     * @return 返回所有用户列表
     */
    @Select("SELECT * FROM users")
    @Results({
            @Result(property = "userName", column = "user_name"),
            @Result(property = "passWord", column = "pass_word"),
            @Result(property = "gender", column = "gender", javaType = GenderEnum.class),
            @Result(property = "nickName", column = "nick_name")
    })
    List<User> getAllUsers();

    /**
     * 根据id查询单条用户数据
     *
     * @param id 传入id
     * @return 返回用户数据
     */
    @Select("SELECT * FROM users WHERE id=#{id}")
    @Results({
            @Result(property = "userName", column = "user_name"),
            @Result(property = "passWord", column = "pass_word"),
            @Result(property = "gender", column = "gender", javaType = GenderEnum.class),
            @Result(property = "nickName", column = "nick_name")
    })
    User getOne(Long id);

    /**
     * 根据性别查询用户
     * Param("表的字段名")
     *
     * @param gender 性别
     * @return 返回用户数据
     */
    @Select("SELECT * FROM users WHERE gender=#{gender}")
    @Results({
            @Result(property = "userName", column = "user_name"),
            @Result(property = "passWord", column = "pass_word"),
            @Result(property = "gender", column = "gender", javaType = GenderEnum.class),
            @Result(property = "nickName", column = "nick_name")
    })
    List<User> getListUserByUserGender(@Param("gender") String gender);

    /**
     * 根据用户名和性别查询用户
     *
     * @param map map
     * @return 返回用户数据
     */
    @Select("SELECT * FROM users WHERE user_name=#{userName} AND gender = #{gender}")
    @Results({
            @Result(property = "userName", column = "user_name"),
            @Result(property = "passWord", column = "pass_word"),
            @Result(property = "gender", column = "gender", javaType = GenderEnum.class),
            @Result(property = "nickName", column = "nick_name")
    })
    List<User> getListUserByNameAndGender(Map<String, Object> map);

    /**
     * 新增用户
     *
     * @param user 用户信息
     * @return 影响行数
     */
    @Insert("INSERT INTO users(user_name, pass_word, gender, nick_name) VALUES(#{userName}, #{passWord}, #{gender}, #{nickName})")
    int insertUser(User user);

    /**
     * 修改用户信息
     *
     * @param user 用户信息
     * @return 影响行数
     */
    @Update("UPDATE users SET user_name=#{userName},nick_name=#{nickName} WHERE id =#{id}")
    int update(User user);

    /**
     * 修改用户信息
     *
     * @param user 用户信息
     * @return 影响行数
     */
    @Update({"<script> ",
            "update users ",
            "<set>",
            " <if test='user_name != null'>userName=#{userName},</if>",
            " <if test='nick_name != null'>nick_name=#{nickName},</if>",
            " </set> ",
            "where id=#{id} ",
            "</script>"})
    int updateUser(User user);

    /**
     * 根据id删除用户信息
     *
     * @param id 表的主键
     * @return 影响行数
     */
    @Delete("DELETE FROM users WHERE id =#{id}")
    int deleteUser(Long id);

    /**
     * 分页查询数据
     *
     * @param userParam user的分类
     * @return 返回数据集合
     */
    @SelectProvider(type = UserSql.class, method = "getList")
    List<User> getList(UserParam userParam);

    /**
     * 统计出查询结果的总数
     *
     * @param userParam user的分类
     * @return 返回总数
     */
    @SelectProvider(type = UserSql.class, method = "getCount")
    int getCount(UserParam userParam);
}
