package com.enterprise.base.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户答题
 * @Description UserXQuestionsEntity
 * 
 * @Author shisan
 * @Date 2018/3/23 上午11:26
 */
public class UserXQuestionsEntity implements Serializable {

    /**
     * 自增主键
     */
    private Integer id;

    private Integer companyId;

    /**
     * 员工Id
     */
    private Integer userId;

    /**
     * 题目Id
     */
    private Integer questionId;

    /**
     * type=0 自主学习
     * type=1 擂台
     * */
    private Integer type;

    /**
     * 答题的状态(0:未答题 1:答题正确 2:答题错误)
     */
    private Integer answerStatus;

    /**
     * 答题使用的时间(单位：秒)
     */
    private Integer answerTime;

    /**
     * 小衰神优于丘比特
     * */
    private Integer toolType;

    /**
     * 状态(0:删除，1:正常 , 2:重新学习 ,3:道具作用下的)
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setAnswerStatus(Integer answerStatus) {
        this.answerStatus = answerStatus;
    }

    public Integer getAnswerStatus() {
        return answerStatus;
    }

    public void setAnswerTime(Integer answerTime) {
        this.answerTime = answerTime;
    }

    public Integer getAnswerTime() {
        return answerTime;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getToolType() {
        return toolType;
    }

    public void setToolType(Integer toolType) {
        this.toolType = toolType;
    }

    public UserXQuestionsEntity() {
    }

    public UserXQuestionsEntity(Integer companyId, Integer userId, Integer questionId, Integer type, Integer answerStatus, Integer answerTime, Integer toolType, Integer status, Date createTime, Date updateTime) {
        this.companyId = companyId;
        this.userId = userId;
        this.questionId = questionId;
        this.type = type;
        this.answerStatus = answerStatus;
        this.answerTime = answerTime;
        this.toolType = toolType;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }



    public UserXQuestionsEntity(Integer companyId,Integer userId, Integer questionId, Integer type, Integer answerStatus, Integer answerTime, Integer status, Date createTime, Date updateTime) {
        this.companyId = companyId;
        this.userId = userId;
        this.questionId = questionId;
        this.type = type;
        this.answerStatus = answerStatus;
        this.answerTime = answerTime;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
