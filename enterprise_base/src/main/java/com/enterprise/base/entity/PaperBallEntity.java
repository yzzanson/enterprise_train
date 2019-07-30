package com.enterprise.base.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/10/23 上午10:31
 */
public class PaperBallEntity {

    private Integer id;

    private Integer companyId;

    private Integer userId;

    //纸团类型
    private Integer ballType;

    //纸团类型有3种
    private Integer type;

    private Integer cleanUserId;

    private Date createTime;

    private Date cleanTime;

    private Date elimateTime;

    // 0/1 被清理/未被清理
    private Integer status;

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getBallType() {
        return ballType;
    }

    public void setBallType(Integer ballType) {
        this.ballType = ballType;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getCleanUserId() {
        return cleanUserId;
    }

    public void setCleanUserId(Integer cleanUserId) {
        this.cleanUserId = cleanUserId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCleanTime() {
        return cleanTime;
    }

    public void setCleanTime(Date cleanTime) {
        this.cleanTime = cleanTime;
    }

    public Date getElimateTime() {
        return elimateTime;
    }

    public void setElimateTime(Date elimateTime) {
        this.elimateTime = elimateTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }



    public PaperBallEntity() {
    }

    public PaperBallEntity(Integer id, Integer cleanUserId, Date cleanTime, Integer status) {
        this.id = id;
        this.cleanUserId = cleanUserId;
        this.cleanTime = cleanTime;
        this.status = status;
    }

    public PaperBallEntity(Integer companyId, Integer userId, Integer cleanUserId, Date createTime, Date cleanTime, Date elimateTime, Integer status) {
        this.companyId = companyId;
        this.userId = userId;
        this.cleanUserId = cleanUserId;
        this.createTime = createTime;
        this.cleanTime = cleanTime;
        this.elimateTime = elimateTime;
        this.status = status;
    }

    public PaperBallEntity(Integer companyId, Integer userId, Integer ballType, Integer cleanUserId, Date createTime, Date cleanTime, Date elimateTime, Integer status) {
        this.companyId = companyId;
        this.userId = userId;
        this.ballType = ballType;
        this.cleanUserId = cleanUserId;
        this.createTime = createTime;
        this.cleanTime = cleanTime;
        this.elimateTime = elimateTime;
        this.status = status;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
