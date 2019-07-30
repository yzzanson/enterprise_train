package com.enterprise.base.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/7/5 下午6:48
 */
public class RightResourceUserVO {

    private Integer id;

    private String name;

    private Integer parentId;

    private Integer order;

    private Integer type;

    List<RightResourceUserVO> subList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<RightResourceUserVO> getSubList() {
        return subList;
    }

    public void setSubList(List<RightResourceUserVO> subList) {
        this.subList = subList;
    }

    public RightResourceUserVO() {
    }

    public RightResourceUserVO(Integer id, String name, Integer parentId, Integer order, Integer type) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.order = order;
        this.type = type;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
