package com.enterprise.base.vo;

/**
 * @Description 宠物蛋经验
 * @Author zezhouyang
 * @Date 18/4/13 下午2:51
 */
public class ExperienceVO {

    private Integer level;

    private Integer isLevelUp;

    private Integer experience;

    private Integer maxExperience;

    private Integer physicalValue;

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getIsLevelUp() {
        return isLevelUp;
    }

    public void setIsLevelUp(Integer isLevelUp) {
        this.isLevelUp = isLevelUp;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public Integer getMaxExperience() {
        return maxExperience;
    }

    public void setMaxExperience(Integer maxExperience) {
        this.maxExperience = maxExperience;
    }

    public Integer getPhysicalValue() {
        return physicalValue;
    }

    public void setPhysicalValue(Integer physicalValue) {
        this.physicalValue = physicalValue;
    }

    public ExperienceVO() {
    }

    public ExperienceVO(Integer level, Integer isLevelUp, Integer experience, Integer maxExperience, Integer physicalValue) {
        this.level = level;
        this.isLevelUp = isLevelUp;
        this.experience = experience;
        this.maxExperience = maxExperience;
        this.physicalValue = physicalValue;
    }
}

