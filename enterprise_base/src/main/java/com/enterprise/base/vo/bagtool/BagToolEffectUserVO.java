package com.enterprise.base.vo.bagtool;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * @Description 用户被作用的道具
 * @Author zezhouyang
 * @Date 18/9/7 下午5:54
 */
public class BagToolEffectUserVO{

    private Integer id;

    private Integer toolId;

    private String toolName;

    private Integer count;

    private String descript;

    private String explain;

    private String tip;

    private List<BagToolEffectDetailVO> effectDetailList;

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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public List<BagToolEffectDetailVO> getEffectDetailList() {
        return effectDetailList;
    }


    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public void setEffectDetailList(List<BagToolEffectDetailVO> effectDetailList) {
        this.effectDetailList = effectDetailList;
    }

    public BagToolEffectUserVO() {
    }

    public BagToolEffectUserVO(Integer id, Integer toolId, Integer count) {
        this.id = id;
        this.toolId = toolId;
        this.count = count;
    }

    public BagToolEffectUserVO(Integer toolId, String toolName, Integer count, String descript, List<BagToolEffectDetailVO> effectDetailList) {
        this.toolId = toolId;
        this.toolName = toolName;
        this.count = count;
        this.descript = descript;
        this.effectDetailList = effectDetailList;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
