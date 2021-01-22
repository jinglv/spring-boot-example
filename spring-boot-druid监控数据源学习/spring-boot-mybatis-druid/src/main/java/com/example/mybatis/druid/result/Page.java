package com.example.mybatis.druid.result;

import com.example.mybatis.druid.param.PageParam;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页封装数据
 * Page将分页信息和数据信息进行封装，方便前端显示第几页、总条数和数据，这样分页功能就完成了
 *
 * @author jingLv
 * @date 2020/09/27
 */
@Data
public class Page<E> implements Serializable {
    private static final long serialVersionUID = -2020350783443768083L;

    /**
     * 当前页数
     */
    private int currentPage = 1;
    /**
     * 总页数
     */
    private long totalPage;
    /**
     * 总记录数
     */
    private long totalNumber;
    /**
     * 数据集
     */
    private List<E> list;

    public static Page<Object> NULL = new Page<>(0, 0, 15, new ArrayList<>());

    public Page() {
        super();
    }

    /**
     * @param beginLine   当前页数
     * @param totalNumber 总记录数
     * @param pageSize    页大小
     * @param list        页数据
     */
    public Page(int beginLine, long totalNumber, int pageSize, List<E> list) {
        super();
        this.currentPage = beginLine / pageSize + 1;
        this.totalNumber = totalNumber;
        this.list = list;
        this.totalPage = totalNumber % pageSize == 0 ? totalNumber
                / pageSize : totalNumber / pageSize + 1;
    }

    public Page(PageParam pageParam, long totalNumber, List<E> list) {
        super();
        this.currentPage = pageParam.getCurrentPage();
        this.totalNumber = totalNumber;
        this.list = list;
        this.totalPage = totalNumber % pageParam.getPageSize() == 0 ? totalNumber
                / pageParam.getPageSize() : totalNumber / pageParam.getPageSize() + 1;
    }
}
