package com.enterprise.base.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

/**
 * QuestionsFeedbackEntity
 *
 * @Author shisan
 * @Date 2018/3/29 下午6:05
 */
public class QuestionsFeedbackEntity {

    /**
     * 自增主键
     */
    private Integer id;

    /**
     * 反馈题目id
     */
    private Integer questionId;

    /**
     * 反馈人Id
     */
    private Integer userId;

    /**
     * 反馈处理人Id
     */
    private Integer handleUserId;

    /**
     * 反馈的内容
     */
    private String content;

    /**
     * 状态(0:删除，1:正常 2.已读 3.已读,已处理)
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

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setHandleUserId(Integer handleUserId) {
        this.handleUserId = handleUserId;
    }

    public Integer getHandleUserId() {
        return handleUserId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
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


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
