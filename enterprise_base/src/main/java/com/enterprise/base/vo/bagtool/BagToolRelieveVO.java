package com.enterprise.base.vo.bagtool;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/9/6 下午2:24
 */
public class BagToolRelieveVO {

    private Integer count;

    private String toolName;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getToolName() {
        return toolName;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    public BagToolRelieveVO() {
    }

    public BagToolRelieveVO(Integer count, String toolName) {
        this.count = count;
        this.toolName = toolName;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
