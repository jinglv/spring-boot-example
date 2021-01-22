CREATE DATABASE shiro DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`
(
    `id`       int(6) NOT NULL AUTO_INCREMENT,
    `username` varchar(40)  DEFAULT NULL,
    `password` varchar(40)  DEFAULT NULL,
    `salt`     varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8;