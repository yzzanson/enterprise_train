package com.enterprise.base.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/6/27 下午5:07
 */
public class QuestionLibraryTitleEntity implements Serializable{

    private static final long serialVersionUID = 526324944915280489L;

    private Integer id;

    private Integer companyId;

    private Integer libraryId;

    private String title;

    private Integer type;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(Integer libraryId) {
        this.libraryId = libraryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public QuestionLibraryTitleEntity() {
    }

    public QuestionLibraryTitleEntity(Integer companyId, Integer libraryId, String title,Date updateTime) {
        this.companyId = companyId;
        this.libraryId = libraryId;
        this.title = title;
        this.updateTime = updateTime;
    }

    public QuestionLibraryTitleEntity(Integer companyId, Integer libraryId, String title, Integer type, Date updateTime) {
        this.companyId = companyId;
        this.libraryId = libraryId;
        this.title = title;
        this.type = type;
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
