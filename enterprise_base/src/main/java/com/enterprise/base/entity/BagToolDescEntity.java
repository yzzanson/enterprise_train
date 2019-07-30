package com.enterprise.base.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @Description 背包道具
 * @Author zezhouyang
 * @Date 18/9/5 下午2:29
 */
public class BagToolDescEntity {

    private Integer id;

    //玩法介绍
    private String playDesc;

    //道具获取方法
    private String gainTool;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPlayDesc() {
        return playDesc;
    }

    public void setPlayDesc(String playDesc) {
        this.playDesc = playDesc;
    }

    public String getGainTool() {
        return gainTool;
    }

    public void setGainTool(String gainTool) {
        this.gainTool = gainTool;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }


}
