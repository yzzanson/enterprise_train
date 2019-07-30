package com.enterprise.base.vo.bagtool;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/9/5 下午2:54
 */
public class BagToolVO {

    private Integer id;

    private String toolName;

    private String descript;

    private String relieve;

    private String exlpainShow;

    private String tip;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getToolName() {
        return toolName;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public String getRelieve() {
        return relieve;
    }

    public void setRelieve(String relieve) {
        this.relieve = relieve;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getExlpainShow() {
        return exlpainShow;
    }

    public void setExlpainShow(String exlpainShow) {
        this.exlpainShow = exlpainShow;
    }

    public BagToolVO() {
    }

    public BagToolVO(String toolName, String descript, String relieve) {
        this.toolName = toolName;
        this.descript = descript;
        this.relieve = relieve;
    }

    public BagToolVO(String toolName, String descript, String relieve, String exlpainShow) {
        this.toolName = toolName;
        this.descript = descript;
        this.relieve = relieve;
        this.exlpainShow = exlpainShow;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
