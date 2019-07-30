package com.enterprise.base.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @Description 题库详情
 * Created by anson on 18/3/26.
 */
public class QuestionLibraryDetailVO extends QuestionLibrarySimpleVO {

    private Integer status;

    private String label;

    private Integer subject;

    /**
     * 排序规则 0/1 随机/顺序
     * */
    private Integer sortType;

    private Integer optionSortType;

    private Integer isOA;

    /**
     * 头衔
     * */
    private String title;

    private Integer titleType;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getSubject() {
        return subject;
    }

    public void setSubject(Integer subject) {
        this.subject = subject;
    }

    public Integer getSortType() {
        return sortType;
    }

    public void setSortType(Integer sortType) {
        this.sortType = sortType;
    }

    public Integer getOptionSortType() {
        return optionSortType;
    }

    public void setOptionSortType(Integer optionSortType) {
        this.optionSortType = optionSortType;
    }

    public Integer getIsOA() {
        return isOA;
    }

    public void setIsOA(Integer isOA) {
        this.isOA = isOA;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTitleType() {
        return titleType;
    }

    public void setTitleType(Integer titleType) {
        this.titleType = titleType;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
