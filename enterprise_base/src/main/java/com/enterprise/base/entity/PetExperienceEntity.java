package com.enterprise.base.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/4/2 下午7:50
 */
public class PetExperienceEntity {

    private Integer id;

    private Integer level;

    private Integer experienceLow;

    private Integer experienceHigh;

    //升级获得体力
    private Integer physicalValue;

    //等级最大体力
    private Integer maxPhysicalValue;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    public Integer getPhysicalValue() {
        return physicalValue;
    }

    public void setPhysicalValue(Integer physicalValue) {
        this.physicalValue = physicalValue;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getExperienceLow() {
        return experienceLow;
    }

    public void setExperienceLow(Integer experienceLow) {
        this.experienceLow = experienceLow;
    }

    public Integer getExperienceHigh() {
        return experienceHigh;
    }

    public void setExperienceHigh(Integer experienceHigh) {
        this.experienceHigh = experienceHigh;
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

    public Integer getMaxPhysicalValue() {
        return maxPhysicalValue;
    }

    public void setMaxPhysicalValue(Integer maxPhysicalValue) {
        this.maxPhysicalValue = maxPhysicalValue;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
