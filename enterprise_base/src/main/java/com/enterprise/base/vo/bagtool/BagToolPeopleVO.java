package com.enterprise.base.vo.bagtool;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/9/5 下午2:54
 */
public class BagToolPeopleVO {

    private Integer toolId;

    private String toolName;

    private Integer count;

    private String descript;

    //该道具的id
    List<Integer> peopleToolIdList;

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


    public Integer getToolId() {
        return toolId;
    }

    public void setToolId(Integer toolId) {
        this.toolId = toolId;
    }

    public List<Integer> getPeopleToolIdList() {
        return peopleToolIdList;
    }

    public void setPeopleToolIdList(List<Integer> peopleToolIdList) {
        this.peopleToolIdList = peopleToolIdList;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public BagToolPeopleVO() {
    }

    public BagToolPeopleVO(Integer toolId, String toolName,String descript, Integer count) {
        this.toolId = toolId;
        this.toolName = toolName;
        this.descript = descript;
        this.count = count;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
