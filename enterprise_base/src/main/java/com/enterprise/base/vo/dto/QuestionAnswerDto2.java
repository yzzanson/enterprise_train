package com.enterprise.base.vo.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by anson on 18/4/2.
 */
public class QuestionAnswerDto2 {

    private Integer companyId;

    private Integer libraryId;

    private Integer userId;

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(Integer libraryId) {
        this.libraryId = libraryId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public QuestionAnswerDto2() {
    }

    public QuestionAnswerDto2(Integer companyId, Integer libraryId, Integer userId) {
        this.companyId = companyId;
        this.libraryId = libraryId;
        this.userId = userId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
