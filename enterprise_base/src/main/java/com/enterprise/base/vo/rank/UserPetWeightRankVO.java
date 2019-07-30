package com.enterprise.base.vo.rank;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @Description 用户答题排名
 * @Author zezhouyang
 * @Date 18/4/2 下午4:49
 */
public class UserPetWeightRankVO {

    private Integer userId;

    //昵称
    private String name;

    //头像
    private String headImage;

    //排名
    private Integer rank;

    //体重
    private Integer answerCount;

    private Integer certCount;

    private Integer titleCount;

    //头衔
    private String title;

    //头衔类型
    private Integer titleType;

    //是否有纸团
    private Integer paperBall;

    //是否玩过,没有则要邀请
    private Integer isNewUser;


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

    public Integer getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(Integer answerCount) {
        this.answerCount = answerCount;
    }

    public Integer getCertCount() {
        return certCount;
    }

    public void setCertCount(Integer certCount) {
        this.certCount = certCount;
    }

    public Integer getTitleCount() {
        return titleCount;
    }

    public void setTitleCount(Integer titleCount) {
        this.titleCount = titleCount;
    }

    public Integer getPaperBall() {
        return paperBall;
    }

    public void setPaperBall(Integer paperBall) {
        this.paperBall = paperBall;
    }

    public Integer getIsNewUser() {
        return isNewUser;
    }

    public void setIsNewUser(Integer isNewUser) {
        this.isNewUser = isNewUser;
    }

    public UserPetWeightRankVO() {
    }

    public UserPetWeightRankVO(Integer userId, String name, String headImage, Integer rank, Integer answerCount) {
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

