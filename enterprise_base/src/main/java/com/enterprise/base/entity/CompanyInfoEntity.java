package com.enterprise.base.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description CompanyInfoEntity
 * @Author shisan
 * @Date 2018/3/23 上午10:19
 */
public class CompanyInfoEntity implements Serializable{

    private static final long serialVersionUID = 526324944923286489L;
    /**
     * 自增主键
     */
    private Integer id;

    /**
     * 企业名称(即在钉钉创建的组织架构的名称)
     */
    private String name;

    /**
     * 是否给学习题库的员工发送消息通知(1:发送 0:不发送) SendOAEnum
     */
    private Integer sendOa;

    /**
     * 微应用的状态(0:卸载，1:正常使用 2:停用) AgentStatusEnum
     */
    private Integer agentStatus;

    /**
     * 状态(0:删除，1:正常)
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private Date stopTime;

    private Date deleteTime;

    private Date refreshTime;

    public CompanyInfoEntity() {
    }

    public CompanyInfoEntity(Integer id, Integer status) {
        this.id = id;
        this.status = status;
    }

    public CompanyInfoEntity(Integer id, Date refreshTime) {
        this.id = id;
        this.refreshTime = refreshTime;
    }

    public CompanyInfoEntity(Integer id) {
        this.id = id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setSendOa(Integer sendOa) {
        this.sendOa = sendOa;
    }

    public Integer getSendOa() {
        return sendOa;
    }

    public void setAgentStatus(Integer agentStatus) {
        this.agentStatus = agentStatus;
    }

    public Integer getAgentStatus() {
        return agentStatus;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public Date getStopTime() {
        return stopTime;
    }

    public void setStopTime(Date stopTime) {
        this.stopTime = stopTime;
    }

    public Date getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }

    public Date getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(Date refreshTime) {
        this.refreshTime = refreshTime;
    }



    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
