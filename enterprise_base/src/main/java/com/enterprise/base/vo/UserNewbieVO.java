package com.enterprise.base.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by anson on 18/4/2.
 */
public class UserNewbieVO {

    private Integer userId;

    //是否选择宠物
    private Integer isChoosePet;

    //是否答题过
    private Integer isAnswered;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getIsChoosePet() {
        return isChoosePet;
    }

    public void setIsChoosePet(Integer isChoosePet) {
        this.isChoosePet = isChoosePet;
    }

    public Integer getIsAnswered() {
        return isAnswered;
    }

    public void setIsAnswered(Integer isAnswered) {
        this.isAnswered = isAnswered;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
