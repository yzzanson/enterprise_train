package com.enterprise.base.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @Description
 * @Author zezhouyang
 * @Date 19/2/12 下午1:48
 */
public class GrainActivityVO {

    private Integer id;

    private String grainBrand;

    private String grainLogo;

    private String grainType;

    private Integer grainCost;

    private Integer needCost;

    private Integer grainCount;

    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGrainBrand() {
        return grainBrand;
    }

    public void setGrainBrand(String grainBrand) {
        this.grainBrand = grainBrand;
    }

    public String getGrainLogo() {
        return grainLogo;
    }

    public void setGrainLogo(String grainLogo) {
        this.grainLogo = grainLogo;
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

    public Integer getNeedCost() {
        return needCost;
    }

    public void setNeedCost(Integer needCost) {
        this.needCost = needCost;
    }

    public Integer getGrainCount() {
        return grainCount;
    }

    public void setGrainCount(Integer grainCount) {
        this.grainCount = grainCount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
