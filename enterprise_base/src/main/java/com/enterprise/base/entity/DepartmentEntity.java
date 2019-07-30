package com.enterprise.base.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

/**
 * DepartmentEntity
 *
 * @Author shisan
 * @Date 2018/3/23 上午10:23
 */
public class DepartmentEntity {

    /**
     * 自增主键
     */
    private Integer id;

    /**
     * 部门所在公司的Id
     */
    private Integer companyId;

    /**
     * 部门在钉钉里的Id
     */
    private Integer dingDeptId;

    /**
     * 部门名称
     */
    private String name;

    /**
     * 状态(0:删除，1:正常)
     */
    private Integer status;

    private Integer parentId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setDingDeptId(Integer dingDeptId) {
        this.dingDeptId = dingDeptId;
    }

    public Integer getDingDeptId() {
        return dingDeptId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public DepartmentEntity() {
    }

    public DepartmentEntity(Integer id, Integer status, Date updateTime) {
        this.id = id;
        this.status = status;
        this.updateTime = updateTime;
    }

    public DepartmentEntity(Integer id, String name, Date updateTime) {
        this.id = id;
        this.name = name;
        this.updateTime = updateTime;
    }

    public DepartmentEntity(Integer companyId, Integer dingDeptId, String name, Integer status, Integer parentId) {
        this.companyId = companyId;
        this.dingDeptId = dingDeptId;
        this.name = name;
        this.status = status;
        this.parentId = parentId;
    }

    public DepartmentEntity(Integer companyId, Integer dingDeptId, String name, Integer status, Integer parentId, Date createTime, Date updateTime) {
        this.companyId = companyId;
        this.dingDeptId = dingDeptId;
        this.name = name;
        this.status = status;
        this.parentId = parentId;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
