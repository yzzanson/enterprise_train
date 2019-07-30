package com.enterprise.base.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/10/23 下午2:14
 */
public class PetFoodVO {

    private Integer userId;

    private Integer petFoodCount;

    private Integer petFoodPlateCount;

    private Integer remainHourTime;

    private Integer remainMinuteTime;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getPetFoodCount() {
        return petFoodCount;
    }

    public void setPetFoodCount(Integer petFoodCount) {
        this.petFoodCount = petFoodCount;
    }

    public Integer getPetFoodPlateCount() {
        return petFoodPlateCount;
    }

    public void setPetFoodPlateCount(Integer petFoodPlateCount) {
        this.petFoodPlateCount = petFoodPlateCount;
    }

    public Integer getRemainHourTime() {
        return remainHourTime;
    }

    public void setRemainHourTime(Integer remainHourTime) {
        this.remainHourTime = remainHourTime;
    }

    public Integer getRemainMinuteTime() {
        return remainMinuteTime;
    }

    public void setRemainMinuteTime(Integer remainMinuteTime) {
        this.remainMinuteTime = remainMinuteTime;
    }

    public PetFoodVO() {
    }

    public PetFoodVO(Integer userId, Integer petFoodCount, Integer petFoodPlateCount, Integer remainHourTime, Integer remainMinuteTime) {
        this.userId = userId;
        this.petFoodCount = petFoodCount;
        this.petFoodPlateCount = petFoodPlateCount;
        this.remainHourTime = remainHourTime;
        this.remainMinuteTime = remainMinuteTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
