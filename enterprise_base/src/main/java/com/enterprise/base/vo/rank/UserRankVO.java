package com.enterprise.base.vo.rank;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @Description
 * @Author zezhouyang
 * @Date 19/3/8 下午2:56
 */
public class UserRankVO {

    private Integer rank;

    private Integer userId;

    private String dingName;

    private String department;

    private String status;

    //累计答题数量
    private Integer studyCount;

    //累计答对题数量
    private Integer rightStudyCount;

    //获取头衔数量
    private Integer titleCount;

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getDingName() {
        return dingName;
    }

    public void setDingName(String dingName) {
        this.dingName = dingName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getStudyCount() {
        return studyCount;
    }

    public void setStudyCount(Integer studyCount) {
        this.studyCount = studyCount;
    }

    public Integer getRightStudyCount() {
        return rightStudyCount;
    }

    public void setRightStudyCount(Integer rightStudyCount) {
        this.rightStudyCount = rightStudyCount;
    }

    public Integer getTitleCount() {
        return titleCount;
    }

    public void setTitleCount(Integer titleCount) {
        this.titleCount = titleCount;
    }

    public UserRankVO() {
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

