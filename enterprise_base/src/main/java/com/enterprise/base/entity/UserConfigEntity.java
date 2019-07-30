package com.enterprise.base.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @Description
 * @Author zezhouyang
 * @Date 19/1/20 下午5:45
 */
public class UserConfigEntity {

    private Integer id;

    private Integer userId;

    private Integer isVoice;

    private Integer isOa;

    private Integer isOnlyWrong;

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

    public Integer getIsVoice() {
        return isVoice;
    }

    public void setIsVoice(Integer isVoice) {
        this.isVoice = isVoice;
    }

    public Integer getIsOa() {
        return isOa;
    }

    public void setIsOa(Integer isOa) {
        this.isOa = isOa;
    }

    public Integer getIsOnlyWrong() {
        return isOnlyWrong;
    }

    public void setIsOnlyWrong(Integer isOnlyWrong) {
        this.isOnlyWrong = isOnlyWrong;
    }

    public UserConfigEntity() {
    }

    public UserConfigEntity(Integer userId, Integer isVoice, Integer isOa, Integer isOnlyWrong) {
        this.userId = userId;
        this.isVoice = isVoice;
        this.isOa = isOa;
        this.isOnlyWrong = isOnlyWrong;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
