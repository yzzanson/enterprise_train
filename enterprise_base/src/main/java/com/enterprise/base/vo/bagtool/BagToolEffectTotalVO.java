package com.enterprise.base.vo.bagtool;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/9/20 下午5:06
 */
public class BagToolEffectTotalVO {

    private Integer userId;

    private Integer count;

    private String name;

    private List<BagToolEffectTotalDetailVO> effectList;


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<BagToolEffectTotalDetailVO> getEffectList() {
        return effectList;
    }

    public void setEffectList(List<BagToolEffectTotalDetailVO> effectList) {
        this.effectList = effectList;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
