package com.enterprise.base.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/12/29 下午2:44
 */
public class MaketBuyVO {

    private String corpId;

    private String corpName;

    private String version;

    private Integer freeDay;

    //0 正常 1过期
    private Integer status;

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getFreeDay() {
        return freeDay;
    }

    public void setFreeDay(Integer freeDay) {
        this.freeDay = freeDay;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public MaketBuyVO() {
    }

    public MaketBuyVO(String corpId, String corpName, String version, Integer freeDay,Integer status) {
        this.corpId = corpId;
        this.corpName = corpName;
        this.version = version;
        this.freeDay = freeDay;
        this.status = status;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }



}
