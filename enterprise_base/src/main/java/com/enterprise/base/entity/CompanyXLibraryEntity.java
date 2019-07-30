package com.enterprise.base.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

/**
 * CompanyXLibraryEntity
 *
 * @Author shisan
 * @Date 2018/3/23 上午10:22
 */
public class CompanyXLibraryEntity {

    /**
     * 自增主键
     */
    private Integer id;

    /**
     * 公司Id
     */
    private Integer companyId;

    /**
     * 题库Id
     */
    private Integer libraryId;

    /**
     * 学习人数
     */
    private Integer userNumber;

    /**
     * 状态(0:删除，1:正常)
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setLibraryId(Integer libraryId) {
        this.libraryId = libraryId;
    }

    public Integer getLibraryId() {
        return libraryId;
    }

    public void setUserNumber(Integer userNumber) {
        this.userNumber = userNumber;
    }

    public Integer getUserNumber() {
        return userNumber;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public CompanyXLibraryEntity() {
    }

    public CompanyXLibraryEntity(Integer id, Integer status) {
        this.id = id;
        this.status = status;
    }

    public CompanyXLibraryEntity(Integer companyId, Integer libraryId, Integer userNumber, Integer status, Date createTime, Date updateTime) {
        this.companyId = companyId;
        this.libraryId = libraryId;
        this.userNumber = userNumber;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public CompanyXLibraryEntity(Integer id, Integer status, Date updateTime) {
        this.id = id;
        this.status = status;
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
