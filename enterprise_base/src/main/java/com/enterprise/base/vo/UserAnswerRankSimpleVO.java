package com.enterprise.base.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @Description 用户答题排名
 * @Author zezhouyang
 * @Date 18/4/2 下午4:49
 */
public class UserAnswerRankSimpleVO {

    private Integer userId;

    //昵称
    private String name;

    //头像
    private String headImage;

    //排名
    private Integer rank;

    //部门
    private String deptName;

    //是否被作用马赛克
    private Integer isMosaic;

    private String title;

    private Integer titleType;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }


    public Integer getIsMosaic() {
        return isMosaic;
    }

    public void setIsMosaic(Integer isMosaic) {
        this.isMosaic = isMosaic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTitleType() {
        return titleType;
    }

    public void setTitleType(Integer titleType) {
        this.titleType = titleType;
    }

    public UserAnswerRankSimpleVO() {
    }

    public UserAnswerRankSimpleVO(Integer userId, String name, String headImage, Integer rank, String deptName) {
        this.userId = userId;
        this.name = name;
        this.headImage = headImage;
        this.rank = rank;
        this.deptName = deptName;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}

