package com.enterprise.base.entity.right;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

/**
 * 菜单表
 *
 * @Author anson
 * @Date 2018/3/23 上午11:49
 */
public class RightResourceUrlEntity {

    private Integer id;

    private String url;

    //父菜单id
    private Integer parentId;

    //菜单名
    private String name;

    //类型（1:菜单 2:url）
    private Integer type;

    //顺序
    private Integer orders;

    private Integer isOperate;

    //是否删除0/1 删除/正常
    private Integer status;

    //创建时间
    private Date createTime;

    //更新时间
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getOrders() {
        return orders;
    }

    public void setOrders(Integer orders) {
        this.orders = orders;
    }

    public Integer getIsOperate() {
        return isOperate;
    }

    public void setIsOperate(Integer isOperate) {
        this.isOperate = isOperate;
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
