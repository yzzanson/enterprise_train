package com.enterprise.base.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/10/30 下午8:51
 */
public class BagToolRemainVO {

    private Integer id;

    private Integer toolId;

    private String toolName;

    private String useToolPeople;

    private Integer totalCount;

    private Integer answerCount;

    private Integer remainCount;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(Integer answerCount) {
        this.answerCount = answerCount;
    }

    public Integer getRemainCount() {
        return remainCount;
    }

    public void setRemainCount(Integer remainCount) {
        this.remainCount = remainCount;
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

    public String getUseToolPeople() {
        return useToolPeople;
    }

    public void setUseToolPeople(String useToolPeople) {
        this.useToolPeople = useToolPeople;
    }

    public BagToolRemainVO() {
    }

    public BagToolRemainVO(Integer id, Integer totalCount, Integer answerCount, Integer remainCount) {
        this.id = id;
        this.totalCount = totalCount;
        this.answerCount = answerCount;
        this.remainCount = remainCount;
    }

    public BagToolRemainVO(Integer id, Integer toolId, String toolName, String useToolPeople,Integer totalCount, Integer answerCount, Integer remainCount) {
        this.id = id;
        this.toolId = toolId;
        this.toolName = toolName;
        this.useToolPeople = useToolPeople;
        this.totalCount = totalCount;
        this.answerCount = answerCount;
        this.remainCount = remainCount;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
