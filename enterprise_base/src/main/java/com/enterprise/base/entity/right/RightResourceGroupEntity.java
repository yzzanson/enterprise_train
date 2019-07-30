package com.enterprise.base.entity.right;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

/**
 * 资源角色关联表
 * Created by anson on 18/3/23.
 */
public class RightResourceGroupEntity {

    private Integer id;

    private Integer companyId;

    //角色表id
    private Integer rightGroupId;

    //资源遍历id
    private Integer rightResourceId;

    //是否删除0/1 删除/正常
    private Integer status;

    private Date createTime;

    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getRightGroupId() {
        return rightGroupId;
    }

    public void setRightGroupId(Integer rightGroupId) {
        this.rightGroupId = rightGroupId;
    }

    public Integer getRightResourceId() {
        return rightResourceId;
    }

    public void setRightResourceId(Integer rightResourceId) {
        this.rightResourceId = rightResourceId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public RightResourceGroupEntity() {
    }



    public RightResourceGroupEntity(Integer companyId, Integer rightGroupId) {
        this.companyId = companyId;
        this.rightGroupId = rightGroupId;
    }

    public RightResourceGroupEntity(Integer companyId, Integer rightGroupId, Integer rightResourceId, Integer status) {
        this.companyId = companyId;
        this.rightGroupId = rightGroupId;
        this.rightResourceId = rightResourceId;
        this.status = status;
    }

    public RightResourceGroupEntity(Integer companyId, Integer rightGroupId, Integer rightResourceId) {
        this.companyId = companyId;
        this.rightGroupId = rightGroupId;
        this.rightResourceId = rightResourceId;
    }

    public RightResourceGroupEntity(Integer companyId, Integer rightGroupId, Integer rightResourceId, Integer status, Date createTime, Date updateTime) {
        this.companyId = companyId;
        this.rightGroupId = rightGroupId;
        this.rightResourceId = rightResourceId;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
