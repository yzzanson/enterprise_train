package com.enterprise.base.entity.bury;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

/**
 * @Description 打开OA消息
 * @Author zezhouyang
 * @Date 18/10/30 下午1:56
 */
public class OAOpenBuryEntity {

    private Integer id;

    private Integer companyId;

    private Integer userId;

    private Integer type;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public OAOpenBuryEntity() {
    }

    public OAOpenBuryEntity(Integer companyId, Integer userId, Integer type, Date createTime) {
        this.companyId = companyId;
        this.userId = userId;
        this.type = type;
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}

