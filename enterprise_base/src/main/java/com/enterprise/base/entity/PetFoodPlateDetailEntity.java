package com.enterprise.base.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

/**
 * @Description 宠物饲料盘
 * @Author zezhouyang
 * @Date 18/10/22 上午11:04
 */
public class PetFoodPlateDetailEntity {

    private Integer id;

    private Integer companyId;

    private Integer userId;

    //喂食人
    private Integer feedUserId;

    private Integer foodCount;

    //PetFoodPlateGainEnum
    private Integer type;

    //0消耗完了 1还没消耗完
    private Integer status;

    private Integer consumePlateId;

    private Date nextPlanConsumeTime;

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getFeedUserId() {
        return feedUserId;
    }

    public void setFeedUserId(Integer feedUserId) {
        this.feedUserId = feedUserId;
    }

    public Integer getFoodCount() {
        return foodCount;
    }

    public void setFoodCount(Integer foodCount) {
        this.foodCount = foodCount;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getConsumePlateId() {
        return consumePlateId;
    }

    public void setConsumePlateId(Integer consumePlateId) {
        this.consumePlateId = consumePlateId;
    }

    public Date getNextPlanConsumeTime() {
        return nextPlanConsumeTime;
    }

    public void setNextPlanConsumeTime(Date nextPlanConsumeTime) {
        this.nextPlanConsumeTime = nextPlanConsumeTime;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public PetFoodPlateDetailEntity() {
    }

    public PetFoodPlateDetailEntity(Integer companyId, Integer userId, Integer feedUserId, Integer foodCount, Integer type, Date createTime, Date updateTime) {
        this.companyId = companyId;
        this.userId = userId;
        this.feedUserId = feedUserId;
        this.foodCount = foodCount;
        this.type = type;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public PetFoodPlateDetailEntity(Integer companyId, Integer userId, Integer feedUserId, Integer foodCount, Integer type,Integer status, Date nextPlanConsumeTime, Date createTime, Date updateTime) {
        this.companyId = companyId;
        this.userId = userId;
        this.feedUserId = feedUserId;
        this.foodCount = foodCount;
        this.type = type;
        this.status = status;
        this.nextPlanConsumeTime = nextPlanConsumeTime;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public PetFoodPlateDetailEntity(Integer companyId, Integer userId, Integer feedUserId, Integer foodCount, Integer type, Integer status, Integer consumePlateId, Date nextPlanConsumeTime, Date createTime, Date updateTime) {
        this.companyId = companyId;
        this.userId = userId;
        this.feedUserId = feedUserId;
        this.foodCount = foodCount;
        this.type = type;
        this.status = status;
        this.consumePlateId = consumePlateId;
        this.nextPlanConsumeTime = nextPlanConsumeTime;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
