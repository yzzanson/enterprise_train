package com.enterprise.base.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

/**
 * MyPetEntity
 *
 * @Author shisan
 * @Date 2018/3/23 上午10:27
 */
public class MyPetEntity {

    /**
     * 自增主键
     */
    private Integer id;

    /**
     * 用户Id
     */
    private Integer userId;

    /**
     * 宠物Id
     */
    private Integer petId;

    /**
     * 宠物名称
     */
    private String petName;

    /**
     * 宠物等级
     */
    private Integer level;

    /**
     * 宠物经验值
     */
    private Integer experienceValue;

    /**
     * 宠物体力值
     */
    private Integer physicalValue;


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

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setPetId(Integer petId) {
        this.petId = petId;
    }

    public Integer getPetId() {
        return petId;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getPetName() {
        return petName;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getLevel() {
        return level;
    }

    public void setExperienceValue(Integer experienceValue) {
        this.experienceValue = experienceValue;
    }

    public Integer getExperienceValue() {
        return experienceValue;
    }

    public void setPhysicalValue(Integer physicalValue) {
        this.physicalValue = physicalValue;
    }

    public Integer getPhysicalValue() {
        return physicalValue;
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

    public MyPetEntity() {
    }

    public MyPetEntity(Integer id) {
        this.id = id;
    }

    public MyPetEntity(Integer id, Integer experienceValue) {
        this.id = id;
        this.experienceValue = experienceValue;
    }

    public MyPetEntity(Integer id, Integer experienceValue, Integer physicalValue) {
        this.id = id;
        this.experienceValue = experienceValue;
        this.physicalValue = physicalValue;
    }

    public MyPetEntity(Integer id, String petName) {
        this.id = id;
        this.petName = petName;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
