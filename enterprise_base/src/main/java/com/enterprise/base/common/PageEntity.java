package com.enterprise.base.common;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

/**
 * 用于接收传来的分页信息在程序内中传值使用
 * Created by Saber on 2016/3/24.
 */
public class PageEntity {

    /**
     * 当前页
     */
    private Integer pageNo;
    /**
     * 每页大小 默认15条
     */
    private Integer pageSize;
    /**
     * 排序字段
     */
    private String sort;
    /**
     * 排序方式 （asc/desc）
     */
    private String order;
    /**
     * 搜索条件
     */
    private String search;

    /**
     * 搜索条件(开始时间)
     */
    private Date beginDate;

    /**
     * 搜索条件(结束时间)
     */
    private Date endDate;

    public PageEntity() {
        this.pageNo = pageNo == null ? 1 : pageNo;
        this.pageSize = pageSize == null ? 15 : pageSize;
    }

    public PageEntity(Integer pageNo, Integer pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public Integer getPageNo() {
        return pageNo == null || pageNo < 0 ? 1 : pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo == null || pageNo < 0 ? 1 : pageNo;
    }

    public Integer getPageSize() {
        return pageSize == null || pageSize < 0 ? 15 : pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize == null || pageSize < 0 ? 15 : pageSize;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
