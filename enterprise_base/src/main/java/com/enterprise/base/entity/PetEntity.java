package com.enterprise.base.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

/**
 * @Description PetEntity
 * @Author shisan
 * @Date 2018/3/23 上午10:28
 */
public class PetEntity {

    /**
     * 自增主键
     */
    private Integer id;

    /**
     * 宠物类型
     */
    private Integer type;

    /**
     * 宠物初始图片
     */
    private String initPic;

    /**
     * 宠物孵化出来后的图片
     */
    private String finalPic;

    /**
     * 状态(0:删除，1:正常)
     */
    private Integer status;

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

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public void setInitPic(String initPic) {
        this.initPic = initPic;
    }

    public String getInitPic() {
        return initPic;
    }

    public void setFinalPic(String finalPic) {
        this.finalPic = finalPic;
    }

    public String getFinalPic() {
        return finalPic;
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
