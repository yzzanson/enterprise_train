package com.enterprise.base.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

/**
 * Created by anson on 18/3/26.
 */
public class UserXCompany {

    /**
     * Id
     */
    private Integer id;

    /**
     * 企业corpId
     */
    private String corpId;

//    /**
//     * 钉钉中(userid)员工唯一标识ID
//     */
//    private String dingId;

    /**
     * 员工在企业内的UserID
     */
    private String dingUserId;

    /**
     * user表中的
     */
    private Integer userId;

    private Integer isAdmin;

    private Integer isBoss;

    /**
     * 超管
     * */
    private Integer isSuperManage;

    /**
     * 状态
     */
    private Integer status;

    private Date createTime;

    private Date updateTime;

    @javax.persistence.Transient
    private Integer source;

    public UserXCompany() {
    }

    public UserXCompany(Integer id) {
        this.id = id;
    }

    public UserXCompany(Integer id, Integer status, Date updateTime) {
        this.id = id;
        this.status = status;
        this.updateTime = updateTime;
    }

    public UserXCompany(Integer id, String dingUserId, String corpId, Integer status, Integer isSuperManage) {
        this.id = id;
        this.dingUserId = dingUserId;
        this.corpId = corpId;
        this.status = status;
        this.isSuperManage = isSuperManage;
    }

    public UserXCompany(Integer id, String corpId, Integer status, Date updateTime, String dingUserId) {
        this.id = id;
        this.corpId = corpId;
        this.status = status;
        this.updateTime = updateTime;
        this.dingUserId = dingUserId;
    }

    public UserXCompany(String corpId, String dingUserId) {
        this.corpId = corpId;
        this.dingUserId = dingUserId;
    }

    public UserXCompany(String corpId, String dingUserId, Integer userId, Integer isAdmin, Integer isBoss, Integer status, Date createTime, Date updateTime, Integer source) {
        this.corpId = corpId;
        this.dingUserId = dingUserId;
        this.userId = userId;
        this.isAdmin = isAdmin;
        this.isBoss = isBoss;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.source = source;
    }

    public UserXCompany(String corpId, String dingUserId, Integer source,Integer status) {
        this.corpId = corpId;
        this.dingUserId = dingUserId;
        this.source = source;
        this.status = status;
    }

    public UserXCompany(String corpId, Integer userId) {
        this.corpId = corpId;
        this.userId = userId;
    }


    public UserXCompany(String corpId, Integer userId,Integer status) {
        this.corpId = corpId;
        this.userId = userId;
        this.status = status;
    }


    public UserXCompany(String corpId,String dingUserId,Integer status) {
        this.corpId = corpId;
        this.dingUserId = dingUserId;
        this.status = status;
    }



    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getDingUserId() {
        return dingUserId;
    }

    public void setDingUserId(String dingUserId) {
        this.dingUserId = dingUserId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Integer isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Integer getIsBoss() {
        return isBoss;
    }

    public void setIsBoss(Integer isBoss) {
        this.isBoss = isBoss;
    }


    public Integer getIsSuperManage() {
        return isSuperManage;
    }

    public void setIsSuperManage(Integer isSuperManage) {
        this.isSuperManage = isSuperManage;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
