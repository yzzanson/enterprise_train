package com.enterprise.base.vo.rank;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @Description
 * @Author zezhouyang
 * @Date 19/3/11 上午9:59
 */
public class UserRankDetailVO {

    private Integer userId;

    private String libraryName;

    //累计答题次数
    private Integer studyCount;

    //累计答对题数量
    private Integer rightStudyCount;

    private String title;

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

    public Integer getRightStudyCount() {
        return rightStudyCount;
    }

    public void setRightStudyCount(Integer rightStudyCount) {
        this.rightStudyCount = rightStudyCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UserRankDetailVO() {
    }

    public UserRankDetailVO(Integer userId, String libraryName, Integer studyCount, Integer rightStudyCount) {
        this.userId = userId;
        this.libraryName = libraryName;
        this.studyCount = studyCount;
        this.rightStudyCount = rightStudyCount;
    }

    public UserRankDetailVO(Integer userId, String libraryName, Integer studyCount, Integer rightStudyCount, String title) {
        this.userId = userId;
        this.libraryName = libraryName;
        this.studyCount = studyCount;
        this.rightStudyCount = rightStudyCount;
        this.title = title;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }


}
