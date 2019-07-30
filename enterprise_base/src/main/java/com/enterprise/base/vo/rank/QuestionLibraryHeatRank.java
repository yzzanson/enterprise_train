package com.enterprise.base.vo.rank;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @Description
 * @Author zezhouyang
 * @Date 19/3/11 下午2:09
 */
public class QuestionLibraryHeatRank {

    private Integer libraryId;

    private String libraryName;

    private Integer studyCount;

    private Integer rank;

    public Integer getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(Integer libraryId) {
        this.libraryId = libraryId;
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

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public QuestionLibraryHeatRank() {
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
