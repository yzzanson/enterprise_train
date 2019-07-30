package com.enterprise.base.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

/**
 * 用户答题
 * @Description UserXTitle
 * 
 * @Author anson
 * @Date 2018/6/28 上午09:55
 */
public class UserXTitleEntity {

    /**
     * 自增主键
     */
    private Integer id;

    private Integer companyId;

    /**
     * 题库id
     */
    private Integer libraryId;

    /**
     * 员工Id
     */
    private Integer userId;

    /**
     * 头衔id
     */
    private Integer titleId;

    private Integer chooseFlag;

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

    public Integer getTitleId() {
        return titleId;
    }

    public void setTitleId(Integer titleId) {
        this.titleId = titleId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getChooseFlag() {
        return chooseFlag;
    }

    public void setChooseFlag(Integer chooseFlag) {
        this.chooseFlag = chooseFlag;
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

    public UserXTitleEntity() {
    }

    public UserXTitleEntity(Integer id, Integer chooseFlag,Integer status ) {
        this.id = id;
        this.status = status;
        this.chooseFlag = chooseFlag;
    }

    public UserXTitleEntity(Integer companyId, Integer libraryId, Integer userId, Integer titleId, Integer chooseFlag, Integer status, Date createTime, Date updateTime) {
        this.companyId = companyId;
        this.libraryId = libraryId;
        this.userId = userId;
        this.titleId = titleId;
        this.chooseFlag = chooseFlag;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
