package com.enterprise.base.vo;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/6/13 下午3:42
 */
public class CompanyLibraryVO {

    private Integer companyId;

    private String compName;

    private String appStatus;

    private Integer isArranged;

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }

    public String getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(String appStatus) {
        this.appStatus = appStatus;
    }

    public Integer getIsArranged() {
        return isArranged;
    }

    public void setIsArranged(Integer isArranged) {
        this.isArranged = isArranged;
    }
}
