package com.enterprise.base.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/4/23 上午11:51
 */
public class StudyRemindEntity {

    private Integer id;

    private Integer companyId;

    private Integer libraryId;

    private Integer deptId;

    private Integer userId;

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
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

    public StudyRemindEntity() {
    }

    public StudyRemindEntity(Integer companyId, Integer libraryId, Integer userId, Integer status, Date createTime, Date updateTime) {
        this.companyId = companyId;
        this.libraryId = libraryId;
        this.userId = userId;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public StudyRemindEntity(Integer id, Integer companyId, Integer libraryId, Integer deptId, Integer userId, Integer status) {
        this.id = id;
        this.companyId = companyId;
        this.libraryId = libraryId;
        this.deptId = deptId;
        this.userId = userId;
        this.status = status;
    }

    public StudyRemindEntity(Integer companyId, Integer libraryId, Integer deptId, Integer userId, Integer status) {
        this.companyId = companyId;
        this.libraryId = libraryId;
        this.deptId = deptId;
        this.userId = userId;
        this.status = status;
    }

    public StudyRemindEntity(Integer id, Integer status, Date updateTime) {
        this.id = id;
        this.status = status;
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }


}
