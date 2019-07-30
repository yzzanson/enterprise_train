package com.enterprise.base.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/4/2 下午7:16
 */
public class MyPetVO {

    private Integer id;

    private Integer userId;

    private String name;

    private String petName;

    private Integer level;

    private Integer weight;

    //当前经验值
    private Integer experienceValue;

    //当前等级最大经验值
    private Integer levelExperienceValue;

    //体力值
    private Integer physicalValue;

    private Integer physicalValueHigh;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getExperienceValue() {
        return experienceValue;
    }

    public void setExperienceValue(Integer experienceValue) {
        this.experienceValue = experienceValue;
    }

    public Integer getPhysicalValue() {
        return physicalValue;
    }

    public void setPhysicalValue(Integer physicalValue) {
        this.physicalValue = physicalValue;
    }

    public Integer getLevelExperienceValue() {
        return levelExperienceValue;
    }

    public void setLevelExperienceValue(Integer levelExperienceValue) {
        this.levelExperienceValue = levelExperienceValue;
    }

    public Integer getPhysicalValueHigh() {
        return physicalValueHigh;
    }

    public void setPhysicalValueHigh(Integer physicalValueHigh) {
        this.physicalValueHigh = physicalValueHigh;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
