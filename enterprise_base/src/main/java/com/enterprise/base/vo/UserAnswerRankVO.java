package com.enterprise.base.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @Description 用户答题排名
 * @Author zezhouyang
 * @Date 18/4/2 下午4:49
 */
public class UserAnswerRankVO {

    private Integer userId;

    //昵称
    private String name;

    //头像
    private String headImage;

    //排名
    private Integer rank;

    //答题次数
    private Integer answerCount;

    //头衔
    private String title;

    //头衔类型
    private Integer titleType;

    //是否有马赛克
    private Integer isMosaic;

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

    public Integer getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(Integer answerCount) {
        this.answerCount = answerCount;
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


    public Integer getIsMosaic() {
        return isMosaic;
    }

    public void setIsMosaic(Integer isMosaic) {
        this.isMosaic = isMosaic;
    }

    public UserAnswerRankVO() {
    }

    public UserAnswerRankVO(Integer userId, String name, String headImage, Integer rank, Integer answerCount) {
        this.userId = userId;
        this.name = name;
        this.headImage = headImage;
        this.rank = rank;
        this.answerCount = answerCount;
    }


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}

