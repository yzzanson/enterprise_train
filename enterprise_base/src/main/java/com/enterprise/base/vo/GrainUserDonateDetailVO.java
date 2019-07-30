package com.enterprise.base.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @Description
 * @Author zezhouyang
 * @Date 19/2/12 下午1:48
 */
public class GrainUserDonateDetailVO {

    private Integer id;

    private String name;

    private String avatar;

    private String grainExplain;

    private String certificateNo;

    private String grainSponsor;

    private Integer grainCost;

    private Integer activityId;

    public GrainUserDonateDetailVO() {
    }

    public GrainUserDonateDetailVO(String name, String avatar, String grainExplain, String certificateNo, String grainSponsor) {
        this.name = name;
        this.avatar = avatar;
        this.grainExplain = grainExplain;
        this.certificateNo = certificateNo;
        this.grainSponsor = grainSponsor;
    }

    public GrainUserDonateDetailVO(String name, String avatar, String grainExplain, String certificateNo, String grainSponsor, Integer grainCost) {
        this.name = name;
        this.avatar = avatar;
        this.grainExplain = grainExplain;
        this.certificateNo = certificateNo;
        this.grainSponsor = grainSponsor;
        this.grainCost = grainCost;
    }

    public GrainUserDonateDetailVO(Integer id, Integer activityId, String name, String avatar, String grainExplain, String certificateNo, String grainSponsor, Integer grainCost) {
        this.id = id;
        this.activityId = activityId;
        this.name = name;
        this.avatar = avatar;
        this.grainExplain = grainExplain;
        this.certificateNo = certificateNo;
        this.grainSponsor = grainSponsor;
        this.grainCost = grainCost;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getGrainExplain() {
        return grainExplain;
    }

    public void setGrainExplain(String grainExplain) {
        this.grainExplain = grainExplain;
    }

    public String getCertificateNo() {
        return certificateNo;
    }

    public void setCertificateNo(String certificateNo) {
        this.certificateNo = certificateNo;
    }

    public String getGrainSponsor() {
        return grainSponsor;
    }

    public void setGrainSponsor(String grainSponsor) {
        this.grainSponsor = grainSponsor;
    }

    public Integer getGrainCost() {
        return grainCost;
    }

    public void setGrainCost(Integer grainCost) {
        this.grainCost = grainCost;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }




    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
