package com.enterprise.base.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * @Description 道具掉落率
 * @Author zezhouyang
 * @Date 18/9/5 下午2:25
 */
public class BagToolRateEntity implements Serializable {

    private Integer id;

    //事件 BagToolGainTypeEnum
    private Integer eventType;

    //道具
    private Integer toolId;

    private Integer rate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }


}
