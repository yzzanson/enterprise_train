package com.enterprise.base.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * @Description 背包道具
 * @Author zezhouyang
 * @Date 18/9/5 下午2:29
 */
public class BagToolEntity implements Serializable {

    private static final long serialVersionUID = 526424924925280489L;

    private Integer id;

    //道具名
    private String toolName;

    //用法描述
    private String descript;

    //实现
    private String effect;

    //解除
    private String relieve;

    private String oaModel;

    private String explainShow;

    private String tip;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public String getRelieve() {
        return relieve;
    }

    public void setRelieve(String relieve) {
        this.relieve = relieve;
    }

    public String getOaModel() {
        return oaModel;
    }

    public void setOaModel(String oaModel) {
        this.oaModel = oaModel;
    }

    public String getExplainShow() {
        return explainShow;
    }

    public void setExplainShow(String explainShow) {
        this.explainShow = explainShow;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }


}
