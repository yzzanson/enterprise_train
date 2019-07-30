package com.enterprise.base.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * IsvTicketsEntity
 *
 * @Author shisan
 * @Date 2018/3/23 上午10:25
 */
public class IsvTicketsEntity implements Serializable {

    private static final long serialVersionUID = 5261234449252804569L;


    private Integer id;


    private String corpId;

    /**
     * 公司Id
     */
    private Integer companyId;

    /**
     * 套件Id
     */
    private Integer suiteId;

    /**
     * 微应用实例化id,由钉钉分配
     */
    private String corpAgentId;

    /**
     * 企业access_token
     */
    private String corpAccessToken;

    /**
     * 企业ticket
     */
    private String corpTicket;

    /**
     * 企业永久授权码
     */
    private String corpPermanentCode;

    /**
     * 状态(0:删除，1:正常)
     */
    private Integer status;

    /**
     * 是否购买(0:未购买,1:购买)
     * */
    private Integer isBuy;

    private Integer isCall;

    /**
     * 授权开通应用的用户id
     * */
    private String authUserId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setSuiteId(Integer suiteId) {
        this.suiteId = suiteId;
    }

    public Integer getSuiteId() {
        return suiteId;
    }

    public void setCorpAgentId(String corpAgentId) {
        this.corpAgentId = corpAgentId;
    }

    public String getCorpAgentId() {
        return corpAgentId;
    }

    public void setCorpAccessToken(String corpAccessToken) {
        this.corpAccessToken = corpAccessToken;
    }

    public String getCorpAccessToken() {
        return corpAccessToken;
    }

    public void setCorpTicket(String corpTicket) {
        this.corpTicket = corpTicket;
    }

    public String getCorpTicket() {
        return corpTicket;
    }

    public void setCorpPermanentCode(String corpPermanentCode) {
        this.corpPermanentCode = corpPermanentCode;
    }

    public String getCorpPermanentCode() {
        return corpPermanentCode;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public Integer getIsBuy() {
        return isBuy;
    }

    public void setIsBuy(Integer isBuy) {
        this.isBuy = isBuy;
    }

    public String getAuthUserId() {
        return authUserId;
    }

    public void setAuthUserId(String authUserId) {
        this.authUserId = authUserId;
    }

    public Integer getIsCall() {
        return isCall;
    }

    public void setIsCall(Integer isCall) {
        this.isCall = isCall;
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
