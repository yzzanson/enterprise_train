package com.enterprise.util.oa.enums;

/**
 * DepartEnum
 *
 * @author shisan
 * @create 2018-03-09 下午4:48
 **/
public enum DepartEnum {
    /**
     * 网吧钉钉体验站开发部门,用于接收消息一出
     */
    DEVELOPDEPT("64433406", "内训部门",1),

    /**
     * 蓝波的专属OA消息提醒部门
     */
    ACTIVEDEPT("64433406", "激活码销售部门",1);

    private String deptId;

    private String deptName;

    private Integer companyId;

    DepartEnum(String deptId, String deptName, Integer companyId) {
        this.deptId = deptId;
        this.deptName = deptName;
        this.companyId = companyId;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
}
