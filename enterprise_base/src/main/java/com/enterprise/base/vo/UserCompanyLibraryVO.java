package com.enterprise.base.vo;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/11/5 下午2:28
 */
public class UserCompanyLibraryVO {

    Integer userId;

    Integer companyId;

    Integer libraryId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

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

    public UserCompanyLibraryVO() {
    }

    public UserCompanyLibraryVO(Integer userId, Integer companyId, Integer libraryId) {
        this.userId = userId;
        this.companyId = companyId;
        this.libraryId = libraryId;
    }
}
