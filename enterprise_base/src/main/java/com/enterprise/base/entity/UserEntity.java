package com.enterprise.base.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description UserEntity
 * @Author shisan
 * @Date 2018/3/23 上午11:15
 */
public class UserEntity implements Serializable{


    /**
     * id
     */
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 钉钉Id,在钉钉全局范围内标识用户的身份（不可修改）-部分用户可能没有dingId
     */
    private String dingId;

    /**
     * unionid用户唯一标识,不为空
     * */
    private String unionId;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 状态(0:删除，1:正常)
     */
    private Integer status;

    /**
     * 状态(0:后台，1:内训前端)
     */
    private Integer source;
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    public UserEntity() {
    }

    public UserEntity(String dingId) {
        this.dingId = dingId;
    }

    public UserEntity(String name, Integer status) {
        this.name = name;
        this.status = status;
    }


    public UserEntity(Integer id, String name, String avatar, Date updateTime) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.updateTime = updateTime;
    }

    public UserEntity(String name, String dingId, String avatar, Integer status, Integer source, Date createTime, Date updateTime) {
        this.name = name;
        this.dingId = dingId;
        this.avatar = avatar;
        this.status = status;
        this.source = source;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public UserEntity(String name, String dingId, String unionId, String avatar, Integer status, Integer source, Date createTime, Date updateTime) {
        this.name = name;
        this.dingId = dingId;
        this.unionId = unionId;
        this.avatar = avatar;
        this.status = status;
        this.source = source;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getDingId() {
        return dingId;
    }

    public void setDingId(String dingId) {
        this.dingId = dingId;
    }


    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatar() {
        return avatar;
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


    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }


}
