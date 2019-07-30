package com.enterprise.base.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @Description
 * @Author zezhouyang
 * @Date 19/1/28 下午2:24
 */
public class UserConfigVO {

    private Integer id;

    private Integer userId;

    private Integer isVoice;

    private Integer isOa;

    private Integer isOnlyWrong;

    private Integer isNew;


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

    public Integer getIsNew() {
        return isNew;
    }

    public void setIsNew(Integer isNew) {
        this.isNew = isNew;
    }

    public UserConfigVO() {
    }

    public UserConfigVO(Integer id, Integer userId, Integer isVoice, Integer isOa, Integer isOnlyWrong, Integer isNew) {
        this.id = id;
        this.userId = userId;
        this.isVoice = isVoice;
        this.isOa = isOa;
        this.isOnlyWrong = isOnlyWrong;
        this.isNew = isNew;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
