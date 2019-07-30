package com.enterprise.base.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/6/28 上午10:15
 */
public class UserXTitleVO {

    private Integer id;

    private Integer userId;

    private String title;

    private Integer type;

    private Integer chooseFlag;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getChooseFlag() {
        return chooseFlag;
    }

    public void setChooseFlag(Integer chooseFlag) {
        this.chooseFlag = chooseFlag;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public UserXTitleVO() {
    }

    public UserXTitleVO(Integer id, Integer userId, String title, Integer type, Integer chooseFlag) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.type = type;
        this.chooseFlag = chooseFlag;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
