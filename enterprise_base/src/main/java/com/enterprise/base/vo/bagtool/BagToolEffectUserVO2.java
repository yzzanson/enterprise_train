package com.enterprise.base.vo.bagtool;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @Description 用户被作用的道具
 * @Author zezhouyang
 * @Date 18/9/7 下午5:54
 */
public class BagToolEffectUserVO2 {

    private Integer id;

    private Integer toolId;

    private String toolName;

    private String explainShow;

    private Integer isRelieve;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getToolId() {
        return toolId;
    }

    public void setToolId(Integer toolId) {
        this.toolId = toolId;
    }

    public String getToolName() {
        return toolName;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    public String getExplainShow() {
        return explainShow;
    }

    public void setExplainShow(String explainShow) {
        this.explainShow = explainShow;
    }

    public BagToolEffectUserVO2() {
    }

    public Integer getIsRelieve() {
        return isRelieve;
    }

    public void setIsRelieve(Integer isRelieve) {
        this.isRelieve = isRelieve;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
