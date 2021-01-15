package com.example.mybatis.annotation.param;

import lombok.Data;

/**
 * 分页基础类
 *
 * @author jingLv
 * @date 2020/09/27
 */
@Data
public class PageParam {
    /**
     * 起始行
     */
    private int beginLine;
    /**
     * 默认每页3条记录，可以根据前端传参进行修改
     */
    private Integer pageSize = 3;
    /**
     * 当前页
     */
    private Integer currentPage = 0;

}
