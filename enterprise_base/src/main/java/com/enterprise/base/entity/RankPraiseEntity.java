package com.enterprise.base.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/8/15 上午10:30
 */
public class RankPraiseEntity {

    private Integer id;

    private Integer companyId;

    //点赞人
    private Integer userId;

    //被点赞人
    private Integer praiseUserId;

    private Integer type;

    private Date createTime;

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

    public Integer getPraiseUserId() {
        return praiseUserId;
    }

    public void setPraiseUserId(Integer praiseUserId) {
        this.praiseUserId = praiseUserId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public RankPraiseEntity() {
    }

    public RankPraiseEntity(Integer companyId, Integer userId, Integer praiseUserId, Date createTime) {
        this.companyId = companyId;
        this.userId = userId;
        this.praiseUserId = praiseUserId;
        this.createTime = createTime;
    }

    public RankPraiseEntity(Integer companyId, Integer userId, Date createTime) {
        this.companyId = companyId;
        this.userId = userId;
        this.createTime = createTime;
    }



    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
