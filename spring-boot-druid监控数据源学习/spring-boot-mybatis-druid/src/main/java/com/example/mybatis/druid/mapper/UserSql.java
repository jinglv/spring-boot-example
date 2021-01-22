package com.example.mybatis.druid.mapper;

import com.example.mybatis.druid.param.UserParam;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.ObjectUtils;

import java.util.Objects;

/**
 * SQL构建动态SQL
 *
 * @author jingLv
 * @date 2021/01/15
 */
public class UserSql {
    /**
     * 使用 StringBuffer 对 SQL 进行拼接
     *
     * @param userParam
     * @return
     */
    public String getList(UserParam userParam) {
        StringBuilder sql = new StringBuilder("select id, user_name as userName, pass_word as passWord, gender, nick_name as nickName");
        sql.append(" from users where 1=1 ");
        if (userParam != null) {
            if (!ObjectUtils.isEmpty(userParam.getUserName())) {
                sql.append(" and user_name = #{userName}");
            }
            if (!ObjectUtils.isEmpty(userParam.getGender())) {
                sql.append(" and gender = #{gender}");
            }
        }
        sql.append(" order by id desc");
        sql.append(" limit ").append(Objects.requireNonNull(userParam).getBeginLine()).append(",").append(userParam.getPageSize());
        return sql.toString();
    }

    /**
     * 结构化 SQL
     *
     * @param userParam
     * @return
     */
    public String getCount(UserParam userParam) {
        String sql = new SQL() {{
            SELECT("count(1)");
            FROM("users");
            if (!ObjectUtils.isEmpty(userParam.getUserName())) {
                WHERE("user_name = #{userName}");
            }
            if (!ObjectUtils.isEmpty(userParam.getGender())) {
                WHERE("gender = #{gender}");
            }
            //从这个toString可以看出，其内部使用高效的StringBuilder实现SQL拼接
        }}.toString();
        return sql;
    }
}
