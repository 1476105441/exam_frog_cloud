package com.wjs.examfrog.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 分页数据封装类
 */
public class CommonPage<T> {

    @ApiModelProperty("当前页码")
    private Integer pageNum;
    @ApiModelProperty("每页数量")
    private Integer pageSize;
    @ApiModelProperty("总页数")
    private Integer totalPage;
    @ApiModelProperty("总条数")
    private Long total;
    @ApiModelProperty("分页数据")
    private List<T> list; // 采用泛型，适用任何排序


    public static <T> CommonPage<T> restPage(Page<T> pageInfo) {
        CommonPage<T> result = new CommonPage<T>();
        result.setTotalPage((int) pageInfo.getPages());
        result.setPageNum((int) pageInfo.getCurrent());
        result.setPageSize((int) pageInfo.getSize());
        result.setTotal(pageInfo.getTotal());
        result.setList(pageInfo.getRecords());
        return result;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
