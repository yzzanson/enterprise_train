package com.enterprise.base.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

/**
 * Created by null on 2018-03-23 10:47:50
 */
public class SuitesEntity {


    private Integer id;


    private String suiteKey;


    private String suiteSecret;

    /**
     * 用于生成签名,校验回调请求的合法性。本套件下相关应用产生的回调消息都使用该值来解密
     */
    private String token;

    /**
     * 回调消息加解密参数，是AES密钥的Base64编码，用于解密回调消息内容对应的密文。本套件下相关应用产生的回调消息都使用该值来解密。
     */
    private String encodingAesKey;

    /**
     * 会失效,定时刷新
     */
    private String suiteAccessToken;

    /**
     * suite_access_token 过期时间
     */
    private Date suiteAccessTokenExpireTime;

    /**
     * 套件描述
     */
    private String description;

    /**
     * 官方定时推送刷新
     */
    private String suiteTicket;

    /**
     * 微应用ID,企业授权成功通过此id找到isv应用在企业中对应的agentid
     */
    private String corpAppid;

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

    public SuitesEntity() {
    }

    public SuitesEntity(Integer id) {
        this.id = id;
    }

    public SuitesEntity(Integer status, Integer id) {
        this.status = status;
        this.id = id;
    }

    public SuitesEntity(Integer id, String suiteKey, String suiteSecret, String token, String encodingAesKey, String suiteAccessToken, Date suiteAccessTokenExpireTime, String description, String suiteTicket, String corpAppid, Integer status, Date createTime, Date updateTime) {
        this.id = id;
        this.suiteKey = suiteKey;
        this.suiteSecret = suiteSecret;
        this.token = token;
        this.encodingAesKey = encodingAesKey;
        this.suiteAccessToken = suiteAccessToken;
        this.suiteAccessTokenExpireTime = suiteAccessTokenExpireTime;
        this.description = description;
        this.suiteTicket = suiteTicket;
        this.corpAppid = corpAppid;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setSuiteKey(String suiteKey) {
        this.suiteKey = suiteKey;
    }

    public String getSuiteKey() {
        return suiteKey;
    }

    public void setSuiteSecret(String suiteSecret) {
        this.suiteSecret = suiteSecret;
    }

    public String getSuiteSecret() {
        return suiteSecret;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setEncodingAesKey(String encodingAesKey) {
        this.encodingAesKey = encodingAesKey;
    }

    public String getEncodingAesKey() {
        return encodingAesKey;
    }

    public void setSuiteAccessToken(String suiteAccessToken) {
        this.suiteAccessToken = suiteAccessToken;
    }

    public String getSuiteAccessToken() {
        return suiteAccessToken;
    }

    public void setSuiteAccessTokenExpireTime(Date suiteAccessTokenExpireTime) {
        this.suiteAccessTokenExpireTime = suiteAccessTokenExpireTime;
    }

    public Date getSuiteAccessTokenExpireTime() {
        return suiteAccessTokenExpireTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setSuiteTicket(String suiteTicket) {
        this.suiteTicket = suiteTicket;
    }

    public String getSuiteTicket() {
        return suiteTicket;
    }

    public void setCorpAppid(String corpAppid) {
        this.corpAppid = corpAppid;
    }

    public String getCorpAppid() {
        return corpAppid;
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
