package com.enterprise.base.vo.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/9/6 下午6:02
 */
public class BagToolRateDTO {

    private Integer eventType;

    private Integer toolId;

    private Integer startRange;

    private Integer endRange;

    public Integer getEventType() {
        return eventType;
    }

    public void setEventType(Integer eventType) {
        this.eventType = eventType;
    }

    public Integer getToolId() {
        return toolId;
    }

    public void setToolId(Integer toolId) {
        this.toolId = toolId;
    }

    public Integer getStartRange() {
        return startRange;
    }

    public void setStartRange(Integer startRange) {
        this.startRange = startRange;
    }

    public Integer getEndRange() {
        return endRange;
    }

    public void setEndRange(Integer endRange) {
        this.endRange = endRange;
    }

    public BagToolRateDTO() {
    }

    public BagToolRateDTO(Integer eventType, Integer toolId, Integer startRange, Integer endRange) {
        this.eventType = eventType;
        this.toolId = toolId;
        this.startRange = startRange;
        this.endRange = endRange;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }



}
