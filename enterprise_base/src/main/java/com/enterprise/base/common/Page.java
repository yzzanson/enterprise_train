package com.enterprise.base.common;

/**
 * 向前台统一返回分页相关
 * Created by Saber on 2016/5/24.
 */
public class Page {
    private Integer pre_page = 1;       //上一页
    private Integer current_page;       //上一页
    private Integer next_page;          //下一页
    private Integer total_page;         //总页数

    public Page(Integer pre_page, Integer current_page, Integer next_page, Integer total_page) {
        this.pre_page = pre_page;
        this.current_page = current_page;
        this.next_page = next_page;
        this.total_page = total_page;
    }

    @Override
    public String toString() {
        return "page{" +
                "pre_page=" + pre_page +
                ", current_page=" + current_page +
                ", next_page=" + next_page +
                ", total_page=" + total_page +
                '}';
    }

    public Integer getPre_page() {
        return pre_page;
    }

    public void setPre_page(Integer pre_page) {
        this.pre_page = pre_page;
    }

    public Integer getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(Integer current_page) {
        this.current_page = current_page;
    }

    public Integer getNext_page() {
        return next_page;
    }

    public void setNext_page(Integer next_page) {
        this.next_page = next_page;
    }

    public Integer getTotal_page() {
        return total_page;
    }

    public void setTotal_page(Integer total_page) {
        this.total_page = total_page;
    }
}
