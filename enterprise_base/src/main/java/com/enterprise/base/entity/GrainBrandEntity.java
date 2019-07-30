package com.enterprise.base.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

/**
 * @Description
 * @Author zezhouyang
 * @Date 19/2/11 下午5:51
 */
public class GrainBrandEntity {

    private Integer id;

    private String grainSponsor;

    private String grainBrand;

    private Integer status;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGrainSponsor() {
        return grainSponsor;
    }

    public void setGrainSponsor(String grainSponsor) {
        this.grainSponsor = grainSponsor;
    }

    public String getGrainBrand() {
        return grainBrand;
    }

    public void setGrainBrand(String grainBrand) {
        this.grainBrand = grainBrand;
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
