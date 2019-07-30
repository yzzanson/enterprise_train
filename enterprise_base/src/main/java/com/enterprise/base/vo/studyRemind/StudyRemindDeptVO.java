package com.enterprise.base.vo.studyRemind;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/4/24 上午11:50
 */
public class StudyRemindDeptVO {

    private Integer deptId;

    private String name;

    private Integer parentId;

    //是否提醒过
    private Integer isRemind;

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getIsRemind() {
        return isRemind;
    }

    public void setIsRemind(Integer isRemind) {
        this.isRemind = isRemind;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
