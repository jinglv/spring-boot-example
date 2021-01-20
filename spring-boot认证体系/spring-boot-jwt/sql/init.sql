-- 用户表
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id`        int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_name` varchar(80) DEFAULT NULL COMMENT '用户名',
    `pass_word` varchar(40) DEFAULT NULL COMMENT '用户密码',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8;

-- 初始化数据
INSERT INTO `user`(`user_name`, `pass_word`)
VALUES ('admin', 'admin'),
       ('manager', '123456');