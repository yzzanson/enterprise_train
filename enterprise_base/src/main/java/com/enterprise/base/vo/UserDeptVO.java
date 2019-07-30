package com.enterprise.base.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @Description 我可挑衅的人
 * @Author zezhouyang
 * @Date 18/4/26 下午8:06
 */
public class UserDeptVO {

    private Integer userId;

    //昵称
    private String name;

    //头像
    private String avatar;

    //部门id
    private Integer deptId;

    private String deptName;

    private Integer isChallenged;

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

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Integer getIsChallenged() {
        return isChallenged;
    }

    public void setIsChallenged(Integer isChallenged) {
        this.isChallenged = isChallenged;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
