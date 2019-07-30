package com.enterprise.base.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.math.BigDecimal;

/**
 * 用户-题库关系
 * Created by anson on 18/3/27.
 */
public class UserXLibraryVO {

    private Integer id;

    private String name;

    private BigDecimal schedule;

//    private Integer totalCount;

//    private Integer answeredCount;

    private String lastAnswerTime;

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

    public BigDecimal getSchedule() {
        return schedule;
    }

    public void setSchedule(BigDecimal schedule) {
        this.schedule = schedule;
    }

//    public String getLastAnswerTime() {
//        return lastAnswerTime;
//    }

//    public void setLastAnswerTime(String lastAnswerTime) {
//        this.lastAnswerTime = lastAnswerTime;
//    }

//    public Integer getTotalCount() {
//        return totalCount;
//    }

//    public void setTotalCount(Integer totalCount) {
//        this.totalCount = totalCount;
//    }

//    public Integer getAnsweredCount() {
//        return answeredCount;
//    }

//    public void setAnsweredCount(Integer answeredCount) {
//        this.answeredCount = answeredCount;
//    }


    public String getLastAnswerTime() {
        return lastAnswerTime;
    }

    public void setLastAnswerTime(String lastAnswerTime) {
        this.lastAnswerTime = lastAnswerTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
