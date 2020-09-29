package com.example.mybatis.plus.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.mybatis.plus.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jingLv
 * @date 2020/09/28
 */
@SpringBootTest
class UserDaoTest {
    @Autowired
    private UserDao userDao;

    /**
     * 查询所有数据
     */
    @Test
    void testQueryAll() {
        List<User> users = userDao.selectList(null);
        users.forEach(user -> System.out.println("users=" + user));
    }

    /**
     * 名字中包含雨并且年龄小于40
     * user_name like '%雨%' and age<40
     */
    @Test
    void testQueryOne() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // 构造条件
        queryWrapper.like("user_name", "雨").lt("age", 40);
        List<User> users = userDao.selectList(queryWrapper);
        users.forEach(user -> System.out.println("users=" + user));
    }

    /**
     * 名字中包含雨年并且龄大于等于20且小于等于40并且email不为空
     * user_name like '%雨%' and age between 20 and 40 and email is not null
     */
    @Test
    void testQueryTwo() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // 构造条件
        queryWrapper.like("user_name", "雨").between("age", 20, 40).isNotNull("email");
        List<User> users = userDao.selectList(queryWrapper);
        users.forEach(user -> System.out.println("users=" + user));
    }

    /**
     * 名字为王姓或者年龄大于等于25，按照年龄降序排列，年龄相同按照id升序排列
     * user_name like '王%' or age>=25 order by age desc,id asc
     */
    @Test
    void testQueryThree() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // 构造条件
        queryWrapper.likeRight("user_name", "王").or().ge("age", 25).orderByDesc("age").orderByAsc("id");
        List<User> users = userDao.selectList(queryWrapper);
        users.forEach(user -> System.out.println("users=" + user));
    }

    /**
     * 创建日期为2019年2月14日并且直属上级为名字为王姓
     * date_format(create_time,'%Y-%m-%d')='2020-09-29' and manager_id in (select job_id from users where user_name like '王%')
     * date_format(create_time,'%Y-%m-%d')='2020-09-29') 这种方式容易造成SQL注入
     */
    @Test
    void testQueryFour() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // 构造条件
        queryWrapper.apply("date_format(create_time,'%Y-%m-%d')={0}", "2020-09-29")
                .inSql("manager_id", "select job_id from users where user_name like '王%'");
        List<User> users = userDao.selectList(queryWrapper);
        users.forEach(user -> System.out.println("users=" + user));
    }

    /**
     * 名字为王姓并且（年龄小于40或邮箱不为空）
     * user_name like '王%' and (age<40 or email is not null)
     */
    @Test
    void testQueryFive() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // 构造条件
        queryWrapper.likeRight("user_name", "王").and(wq -> wq.lt("age", 40).or().isNotNull("email"));
        List<User> users = userDao.selectList(queryWrapper);
        users.forEach(user -> System.out.println("users=" + user));
    }


    /**
     * （年龄小于40或邮箱不为空）并且名字为王姓
     * (age<40 or email is not null) and user_name like '王%'
     */
    @Test
    void testQuerySix() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // 构造条件
        queryWrapper.likeRight("user_name", "王").or(wq -> wq.lt("age", 40).gt("age", 20).isNotNull("email"));
        List<User> users = userDao.selectList(queryWrapper);
        users.forEach(user -> System.out.println("users=" + user));
    }

    /**
     * 名字为王姓或者（年龄小于40并且年龄大于20并且邮箱不为空）
     * user_name like '王%' or (age<40 and age>20 and email is not null)
     * and优先级高于or
     */
    @Test
    void testQuerySeven() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // 构造条件
        queryWrapper.nested(wq -> wq.lt("age", 40).or().isNotNull("email"))
                .likeRight("user_name", "王");
        List<User> users = userDao.selectList(queryWrapper);
        users.forEach(user -> System.out.println("users=" + user));
    }

    /**
     * 年龄为30、31、34、35
     * age in (30、31、34、35)
     */
    @Test
    void testQueryEight() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // 构造条件
        queryWrapper.in("age", Arrays.asList(30, 31, 34, 35));
        List<User> users = userDao.selectList(queryWrapper);
        users.forEach(user -> System.out.println("users=" + user));
    }

    /**
     * 只返回满足条件的其中一条语句即可
     * limit 1
     * <p>
     * last:无视优化规则直接拼接到sql的最后
     * 注意事项：只能调用一次，多次调用以最后一次为准，有sql注入的风险，请谨慎使用
     */
    @Test
    void testQueryNine() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // 构造条件
        queryWrapper.in("age", Arrays.asList(30, 31, 34, 35)).last("limit 1");
        List<User> users = userDao.selectList(queryWrapper);
        users.forEach(user -> System.out.println("users=" + user));
    }

    /**
     * 名字中包含雨并且年龄小于40
     * select job_id,user_name from users where user_name like '%雨%' and age<40
     */
    @Test
    void testQueryFieldOne() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // 构造条件
        queryWrapper.select("job_id", "user_name").like("user_name", "雨").lt("age", 40);
        List<User> users = userDao.selectList(queryWrapper);
        users.forEach(user -> System.out.println("users=" + user));
    }

    /**
     * 名字中包含雨并且年龄小于40
     * select id,user_name,pass_word, age,email from users where user_name like '%雨%' and age<40
     * 字段较多的情况,直接传实体
     * select可以写在语句的后面
     */
    @Test
    void testQueryFieldTwo() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // 构造条件
        queryWrapper.like("user_name", "雨").lt("age", 40)
                .select(User.class, info -> !info.getColumn().equals("manager_id") &&
                        !info.getColumn().equals("job_id"));
        List<User> users = userDao.selectList(queryWrapper);
        users.forEach(user -> System.out.println("users=" + user));
    }

    @Test
    void testFindOne() {
        User user = userDao.selectById(2);
        System.out.println(user);
    }

    @Test
    void testFindIds() {
        List<Integer> ids = Arrays.asList(1, 2, 3);
        List<User> users = userDao.selectBatchIds(ids);
        users.forEach(System.out::println);
    }

    @Test
    void testFindByMap() {
        Map<String, Object> columnMap = new HashMap<>();
        // key是数据库中的列名，不是User实体类的名称
        columnMap.put("user_name", "王天风");
        columnMap.put("age", 25);
        List<User> users = userDao.selectByMap(columnMap);
        users.forEach(System.out::println);
    }

    public void condition(String userName, String email) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // 这种写法不简洁
        /*if (StringUtils.isNotBlank(userName)) {
            queryWrapper.like("user_name", userName);
        }
        if (StringUtils.isNotBlank(email)) {
            queryWrapper.like("email", email);
        }*/
        // 优化写法
        queryWrapper.like(StringUtils.isNotBlank(userName), "user_name", userName)
                .like(StringUtils.isNotBlank(email), "email", email);

        List<User> users = userDao.selectList(queryWrapper);
        users.forEach(user -> System.out.println("users=" + user));
    }

    @Test
    void testCondition() {
        // 模拟前台传来两个参数
        String name = "王";
        String email = "";
        condition(name, email);
    }
}