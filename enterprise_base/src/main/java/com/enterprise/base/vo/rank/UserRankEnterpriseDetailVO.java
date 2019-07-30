package com.enterprise.base.vo.rank;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @Description
 * @Author zezhouyang
 * @Date 19/3/11 上午9:59
 */
public class UserRankEnterpriseDetailVO {

    private Integer userId;

    private String libraryName;

    //累计答题次数
    private Integer studyCount;

    //完成度
    private String schedule;

    private String accuracy;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getLibraryName() {
        return libraryName;
    }

    public void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
    }

    public Integer getStudyCount() {
        return studyCount;
    }

    public void setStudyCount(Integer studyCount) {
        this.studyCount = studyCount;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public UserRankEnterpriseDetailVO() {
    }

    public UserRankEnterpriseDetailVO(Integer userId, String libraryName, Integer studyCount, String schedule, String accuracy) {
        this.userId = userId;
        this.libraryName = libraryName;
        this.studyCount = studyCount;
        this.schedule = schedule;
        this.accuracy = accuracy;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }


}
