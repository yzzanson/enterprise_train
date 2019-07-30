package com.enterprise.base.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/7/4 下午4:12
 */
public class UserRightGroupVO {

    private Integer userId;

    private String name;

    private String avatar;

    private Integer isSelected;

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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(Integer isSelected) {
        this.isSelected = isSelected;
    }

    public UserRightGroupVO() {
    }

    public UserRightGroupVO(Integer userId, String name, String avatar) {
        this.userId = userId;
        this.name = name;
        this.avatar = avatar;
    }

    public UserRightGroupVO(Integer userId, String name,Integer isSelected) {
        this.userId = userId;
        this.name = name;
        this.isSelected = isSelected;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }


}

