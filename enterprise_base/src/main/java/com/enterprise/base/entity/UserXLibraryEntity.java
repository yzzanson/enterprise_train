package com.enterprise.base.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description UserXLibraryEntity
 * @Author shisan
 * @Date 2018/3/23 上午11:24
 */
public class UserXLibraryEntity {

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
     * 题库Id(questionsLibrary 表的Id)
     */
    private Integer libraryId;

    /**
     * 学习进度(乘以了100)
     */
    private BigDecimal schedule;

    /**
     * 答题数
     * */
    private Integer answerCount;

    /**
     * 最近一次答题时间
     */
    private Date lastAnswerTime = new Date();

    private String finishTime;

    private Integer isUpdate;

    /**
     * 状态(0:删除，1:正常，2:已完成(完成题库全部题目)
     */
    private Integer status = 1;

    /**
     * 创建时间
     */
    private Date createTime = new Date();

    /**
     * 更新时间
     */
    private Date updateTime = new Date();


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

    public void setLibraryId(Integer libraryId) {
        this.libraryId = libraryId;
    }

    public Integer getLibraryId() {
        return libraryId;
    }

    public void setSchedule(BigDecimal schedule) {
        this.schedule = schedule;
    }

    public BigDecimal getSchedule() {
        return schedule;
    }

    public Integer getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(Integer answerCount) {
        this.answerCount = answerCount;
    }

    public void setLastAnswerTime(Date lastAnswerTime) {
        this.lastAnswerTime = lastAnswerTime;
    }

    public Date getLastAnswerTime() {
        return lastAnswerTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
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

    public Integer getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(Integer isUpdate) {
        this.isUpdate = isUpdate;
    }

    public UserXLibraryEntity() {
    }

    public UserXLibraryEntity(Integer companyId,Integer status, Integer libraryId) {
        this.companyId = companyId;
        this.status = status;
        this.libraryId = libraryId;
    }

    public UserXLibraryEntity(Integer id, Integer companyId, Integer userId, Integer status) {
        this.id = id;
        this.companyId = companyId;
        this.userId = userId;
        this.status = status;
    }

    public UserXLibraryEntity(Integer id, BigDecimal schedule, Date updateTime) {
        this.id = id;
        this.schedule = schedule;
        this.updateTime = updateTime;
    }

    public UserXLibraryEntity(Integer id, Date lastAnswerTime, Date updateTime) {
        this.id = id;
        this.lastAnswerTime = lastAnswerTime;
        this.updateTime = updateTime;
    }

    public UserXLibraryEntity(Integer id, BigDecimal schedule, Date lastAnswerTime, Date updateTime) {
        this.id = id;
        this.schedule = schedule;
        this.lastAnswerTime = lastAnswerTime;
        this.updateTime = updateTime;
    }

    public UserXLibraryEntity(Integer userId, Integer libraryId, BigDecimal schedule, Date lastAnswerTime, Integer status, Date createTime, Date updateTime,Integer companyId) {
        this.userId = userId;
        this.libraryId = libraryId;
        this.schedule = schedule;
        this.lastAnswerTime = lastAnswerTime;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.companyId = companyId;
    }

    public UserXLibraryEntity(Integer companyId, Integer userId, Integer libraryId, BigDecimal schedule, Integer answerCount, Date lastAnswerTime, Integer isUpdate, Integer status, Date createTime, Date updateTime) {
        this.companyId = companyId;
        this.userId = userId;
        this.libraryId = libraryId;
        this.schedule = schedule;
        this.answerCount = answerCount;
        this.lastAnswerTime = lastAnswerTime;
        this.isUpdate = isUpdate;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public UserXLibraryEntity(Integer companyId, Integer userId, Integer libraryId, Integer status ,Date updateTime) {
        this.companyId = companyId;
        this.userId = userId;
        this.libraryId = libraryId;
        this.status = status;
        this.updateTime = updateTime;
    }

    public UserXLibraryEntity(Integer id, Integer userId, Integer libraryId, BigDecimal schedule, Date lastAnswerTime, Integer status, Date createTime, Date updateTime) {
        this.id = id;
        this.userId = userId;
        this.libraryId = libraryId;
        this.schedule = schedule;
        this.lastAnswerTime = lastAnswerTime;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }




    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
