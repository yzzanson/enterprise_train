package com.enterprise.base.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/6/15 下午2:17
 */
public class SimpleQuestionLibraryVO {

    private Integer id;

    private Integer companyLibraryId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCompanyLibraryId() {
        return companyLibraryId;
    }

    public void setCompanyLibraryId(Integer companyLibraryId) {
        this.companyLibraryId = companyLibraryId;
    }

    public SimpleQuestionLibraryVO() {
    }

    public SimpleQuestionLibraryVO(Integer id, Integer companyLibraryId) {
        this.id = id;
        this.companyLibraryId = companyLibraryId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
