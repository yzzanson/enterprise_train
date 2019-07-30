package com.enterprise.base.vo.bagtool;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/9/7 上午10:11
 */
public class BagTreasureOpenVO {

    private Integer id;

    private Integer tooleId;

    private String toolName;

    private String descript;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTooleId() {
        return tooleId;
    }

    public void setTooleId(Integer tooleId) {
        this.tooleId = tooleId;
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

    public BagTreasureOpenVO() {
    }

    public BagTreasureOpenVO(Integer id, Integer tooleId, String toolName, String descript) {
        this.id = id;
        this.tooleId = tooleId;
        this.toolName = toolName;
        this.descript = descript;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
