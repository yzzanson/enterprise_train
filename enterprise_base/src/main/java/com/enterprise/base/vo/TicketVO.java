package com.enterprise.base.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

/**
 * 钉钉消息
 * Created by Saber on 2016/8/16.
 */
public class TicketVO {

    /**
     * tickets表主键
     */
    private Integer id;

    /**
     * jsTicket
     */
    private String ticket;
    /**
     * accessToken
     */
    private String accessToken;

    private Integer companyId;

    /**
     * 组织架构Id           （消息发送组织架构）
     */
    private String corpId;
    /**
     * 微应用Id             （消息发送的微应用Id）[ agentid 企业应用id，这个值代表以哪个应用的名义发送消息]
     */
    private String agentId;

    private String departmentId;

    /**
     * 数据最后刷新时间 用于判断accessToken是否过期
     */
    private Date updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
