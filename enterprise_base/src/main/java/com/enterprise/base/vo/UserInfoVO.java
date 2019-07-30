package com.enterprise.base.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/4/16 上午9:53
 */
public class UserInfoVO {

    private Integer id;

    private String dept;

    private String name;

    private String avatar;

    //宠物等级
    private Integer level;

    //体力
    private Integer vit;

    //当前等级体力最大值
    private Integer vitTop;

    //当前经验值
    private Integer exp;

    //当前等级经验最大值
    private Integer expTop;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getVit() {
        return vit;
    }

    public void setVit(Integer vit) {
        this.vit = vit;
    }

    public Integer getExp() {
        return exp;
    }

    public void setExp(Integer exp) {
        this.exp = exp;
    }

    public Integer getExpTop() {
        return expTop;
    }

    public void setExpTop(Integer expTop) {
        this.expTop = expTop;
    }

    public Integer getVitTop() {
        return vitTop;
    }

    public void setVitTop(Integer vitTop) {
        this.vitTop = vitTop;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public UserInfoVO() {
    }

    public UserInfoVO(Integer id, String dept, String name, String avatar,Integer level, Integer vit, Integer exp,Integer expTop) {
        this.id = id;
        this.dept = dept;
        this.name = name;
        this.avatar = avatar;
        this.level = level;
        this.vit = vit;
        this.exp = exp;
        this.expTop = expTop;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
