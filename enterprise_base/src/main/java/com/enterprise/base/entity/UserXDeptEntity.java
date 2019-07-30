package com.enterprise.base.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

/**
 * Created by null on 2018-03-23 11:08:46
 */
public class UserXDeptEntity {

    /**
     * 自增主键
     */
    private Integer id;

    private String corpId;

    /**
     * 部门Id(department表中的Id)
     */
    private Integer deptId;

    private String deptName;

    private String dingUserId;
    /**
     * 员工Id
     */
    private Integer userId;

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

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
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

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDingUserId() {
        return dingUserId;
    }

    public void setDingUserId(String dingUserId) {
        this.dingUserId = dingUserId;
    }

    public UserXDeptEntity() {
    }

    public UserXDeptEntity(String corpId, Integer userId, Integer status) {
        this.corpId = corpId;
        this.userId = userId;
        this.status = status;
    }



    public UserXDeptEntity(Integer deptId, String corpId, String dingUserId) {
        this.deptId = deptId;
        this.corpId = corpId;
        this.dingUserId = dingUserId;
    }

//    public UserXDeptEntity(String deptName, Integer id, Integer status, Date updateTime) {
//        this.deptName = deptName;
//        this.id = id;
//        this.status = status;
//        this.updateTime = updateTime;
//    }


    public UserXDeptEntity(String corpId, Integer userId, Integer deptId, Integer status) {
        this.corpId = corpId;
        this.userId = userId;
        this.deptId = deptId;
        this.status = status;
    }

    public UserXDeptEntity(Integer id,Integer deptId, String deptName, String dingUserId, Integer status, Date updateTime) {
        this.id = id;
        this.deptId = deptId;
        this.deptName = deptName;
        this.dingUserId = dingUserId;
        this.status = status;
        this.updateTime = updateTime;
    }

    public UserXDeptEntity(String corpId, Integer deptId, String deptName, String dingUserId, Integer userId, Integer status, Date createTime, Date updateTime) {
        this.corpId = corpId;
        this.deptId = deptId;
        this.deptName = deptName;
        this.dingUserId = dingUserId;
        this.userId = userId;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
