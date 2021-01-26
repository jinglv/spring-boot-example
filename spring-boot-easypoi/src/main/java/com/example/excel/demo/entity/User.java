package com.example.excel.demo.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author jingLv
 * @date 2021/01/26
 */
@Data
@ExcelTarget("users")
public class User implements Serializable {
    
    private static final long serialVersionUID = -9205298549640800970L;

    @Excel(name = "编号")
    private String id;
    @Excel(name = "姓名")
    private String name;
    @Excel(name = "生日", format = "yyyy年MM月dd日")
    private Date bir;
    @Excel(name = "头像信息", type = 2, savePath = "/Users/apple/JavaProject/spring-boot-example/spring-boot-easypoi/src/main/resources/static/")
    private String photo;
    @Excel(name = "爱好", width = 12.0)
    private Integer age;
    @Excel(name = "状态", replace = {"激活_1", "锁定_0"})
    private String status;
}
