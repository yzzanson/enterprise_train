package com.enterprise.base.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

/**
 * @Description 爱心粮
 * @Author zezhouyang
 * @Date 19/2/11 下午4:39
 */
public class GrainActivityEntity {

    private Integer id;

    //赞助商
    private Integer grainBrandId;

    //爱心物资品牌
    private String grainLogo;

    //爱心物资品牌
    private String grainDetailLogo;

    //分类
    private String grainType;

    //所需爱心
    private Integer grainCost;

    //申请上限
    private Integer grainCount;

    //被捐助方
    private String donatedBase;

    //爱心物资描述
    private String grainExplain;

    //证书编号英文缩写
    private String certificate;

    //证书正文
    private String certificateContent;

    //状态 1正常 2已领完
    private Integer status;

    //创建时间
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGrainBrandId() {
        return grainBrandId;
    }

    public void setGrainBrandId(Integer grainBrandId) {
        this.grainBrandId = grainBrandId;
    }

    public String getGrainLogo() {
        return grainLogo;
    }

    public void setGrainLogo(String grainLogo) {
        this.grainLogo = grainLogo;
    }

    public String getGrainDetailLogo() {
        return grainDetailLogo;
    }

    public void setGrainDetailLogo(String grainDetailLogo) {
        this.grainDetailLogo = grainDetailLogo;
    }

    public String getGrainType() {
        return grainType;
    }

    public void setGrainType(String grainType) {
        this.grainType = grainType;
    }

    public Integer getGrainCost() {
        return grainCost;
    }

    public void setGrainCost(Integer grainCost) {
        this.grainCost = grainCost;
    }

    public Integer getGrainCount() {
        return grainCount;
    }

    public void setGrainCount(Integer grainCount) {
        this.grainCount = grainCount;
    }

    public String getDonatedBase() {
        return donatedBase;
    }

    public void setDonatedBase(String donatedBase) {
        this.donatedBase = donatedBase;
    }

    public String getGrainExplain() {
        return grainExplain;
    }

    public void setGrainExplain(String grainExplain) {
        this.grainExplain = grainExplain;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getCertificateContent() {
        return certificateContent;
    }

    public void setCertificateContent(String certificateContent) {
        this.certificateContent = certificateContent;
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
