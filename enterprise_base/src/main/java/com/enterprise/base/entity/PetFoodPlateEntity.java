package com.enterprise.base.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

/**
 * @Description 宠物饲料盘
 * @Author zezhouyang
 * @Date 18/10/22 上午11:04
 */
public class PetFoodPlateEntity {

    private Integer id;

    private Integer companyId;

    private Integer userId;

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

    public PetFoodPlateEntity() {
    }

    public PetFoodPlateEntity(Integer id, Integer foodCount, Date updateTime) {
        this.id = id;
        this.foodCount = foodCount;
        this.updateTime = updateTime;
    }

    public PetFoodPlateEntity(Integer companyId, Integer userId, Integer foodCount, Date createTime, Date updateTime) {
        this.companyId = companyId;
        this.userId = userId;
        this.foodCount = foodCount;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
