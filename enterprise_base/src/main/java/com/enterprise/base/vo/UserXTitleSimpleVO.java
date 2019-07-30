package com.enterprise.base.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/6/28 下午4:53
 */
public class UserXTitleSimpleVO {

    private String title;

    private Integer titleType;

    private String avatar;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getTitleType() {
        return titleType;
    }

    public void setTitleType(Integer titleType) {
        this.titleType = titleType;
    }

    public UserXTitleSimpleVO() {
    }

    public UserXTitleSimpleVO(String title, String avatar) {
        this.title = title;
        this.avatar = avatar;
    }

    public UserXTitleSimpleVO(String title, Integer titleType, String avatar) {
        this.title = title;
        this.titleType = titleType;
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
