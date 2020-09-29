-- 创建user表
DROP TABLE IF EXISTS users;
CREATE TABLE users
(
    id          BIGINT UNSIGNED AUTO_INCREMENT COMMENT '主键' PRIMARY KEY,
    job_id      BIGINT      DEFAULT NULL COMMENT '工作id',
    user_name   VARCHAR(32) DEFAULT NULL COMMENT '用户名',
    pass_word   VARCHAR(32) DEFAULT NULL COMMENT '密码',
    age         INT(11)     DEFAULT NULL COMMENT '年龄',
    email       VARCHAR(50) DEFAULT NULL COMMENT '邮箱',
    manager_id  BIGINT(20)  DEFAULT NULL COMMENT '直属上级id',
    create_time DATETIME    DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    update_time TIMESTAMP   DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8 COMMENT '用户表';

-- 数据初始化
INSERT INTO users (job_id, user_name, pass_word, age, email, manager_id)
VALUES (1000101, '大Boss', 'admin', 40, 'boss@baomidou.com', NULL),
       (1000102, '王天风', '123456', 25, 'wtf@baomidou.com', 1000101),
       (1000103, '李艺伟', '123123', 28, 'lyw@baomidou.com', 1000102),
       (1000104, '张雨琪', '123123', 31, 'zjq@baomidou.com', 1000102),
       (1000105, '刘红雨', '123123', 32, 'lhm@baomidou.com', 1000102);