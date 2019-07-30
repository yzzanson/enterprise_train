package com.enterprise.base.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

/**
 * @Description
 * @Author zezhouyang
 * @Date 19/3/25 下午2:24
 */
public class CommunityInviteEntity {

    private Integer id;

    private Integer companyId;

    //被邀请人
    private Integer userId;

    //邀请人
    private Integer inviteUser;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getInviteUser() {
        return inviteUser;
    }

    public void setInviteUser(Integer inviteUser) {
        this.inviteUser = inviteUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public CommunityInviteEntity() {
    }

    public CommunityInviteEntity(Integer companyId, Integer userId, Integer inviteUser, Date createTime) {
        this.companyId = companyId;
        this.userId = userId;
        this.inviteUser = inviteUser;
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
