package com.enterprise.base.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

/**
 * @Description 被作用相关保存
 * @Author zezhouyang
 * @Date 18/9/10 下午3:40
 */
public class BagToolEffectQuestionEntity {

    private Integer id;

    private Integer companyId;

    //影响的种类
    private Integer toolId;

    private Integer userId;

    //答题记录
    private Integer userXQuestionId;

    //道具影响记录
    private Integer bagToolEffectId;

    private Integer addExp;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getToolId() {
        return toolId;
    }

    public void setToolId(Integer toolId) {
        this.toolId = toolId;
    }

    public Integer getUserXQuestionId() {
        return userXQuestionId;
    }

    public void setUserXQuestionId(Integer userXQuestionId) {
        this.userXQuestionId = userXQuestionId;
    }

    public Integer getBagToolEffectId() {
        return bagToolEffectId;
    }

    public void setBagToolEffectId(Integer bagToolEffectId) {
        this.bagToolEffectId = bagToolEffectId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getAddExp() {
        return addExp;
    }

    public void setAddExp(Integer addExp) {
        this.addExp = addExp;
    }

    public BagToolEffectQuestionEntity() {
    }

    public BagToolEffectQuestionEntity(Integer id, Integer addExp) {
        this.id = id;
        this.addExp = addExp;
    }

    public BagToolEffectQuestionEntity(Integer companyId, Integer toolId, Integer userId, Integer userXQuestionId, Integer bagToolEffectId, Date createTime) {
        this.companyId = companyId;
        this.toolId = toolId;
        this.userId = userId;
        this.userXQuestionId = userXQuestionId;
        this.bagToolEffectId = bagToolEffectId;
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
