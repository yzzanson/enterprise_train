package com.enterprise.base.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

/**
 * @Description 宠物饲料详情
 * @Author zezhouyang
 * @Date 18/10/22 上午10:41
 */
public class PetFoodDetailEntity {

    private Integer id;

    private Integer companyId;

    private Integer userId;

    //PetFoodGainEnum 1初始化 2答题 3喂食 4打扫 5投食他人 6丘比特对方答题
    private Integer type;

    private Integer foodCount;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getFoodCount() {
        return foodCount;
    }

    public void setFoodCount(Integer foodCount) {
        this.foodCount = foodCount;
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

    public PetFoodDetailEntity() {
    }

    public PetFoodDetailEntity(Integer companyId, Integer userId, Integer type, Integer foodCount, Date createTime, Date updateTime) {
        this.companyId = companyId;
        this.userId = userId;
        this.type = type;
        this.foodCount = foodCount;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
