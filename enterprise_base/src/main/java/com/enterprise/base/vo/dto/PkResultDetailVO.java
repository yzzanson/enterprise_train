package com.enterprise.base.vo.dto;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/4/11 上午10:23
 */
public class PkResultDetailVO {

    /**
     * 问题及答题情况 {questionId:1,description:"dddd", ownerAnswerStatus:1, pkAnswerStatus:1}
     */
    private List<JSONObject> questionInfo;

    /**
     * 擂主Id
     */
    private Integer ownerUserId;

    /**
     * 挑战者Id
     */
    private Integer pkUserId;

    /**
     * 擂主头像
     */
    private String ownerHeadPic;

    /**
     * 挑战者头像
     */
    private String pkHeadPic;

    /**
     * 擂主名称
     */
    private String ownerName;

    /**
     * 挑战者名称
     */
    private String pkName;

    /**
     * 擂主得分
     */
    private Integer ownerScore;

    /**
     * 挑战者得分
     */
    private Integer pkScore;

    /**
     * 擂主答对的题目数
     */
    private Integer ownerRightCount;

    /**
     * 挑战者答对的题目数
     */
    private Integer pkRightCount;

    /**
     * 擂主所在部门名称
     */
    private String ownerDeptName;

    /**
     * 挑战者所在部门名称
     */
    private String pkDeptName;

    /**
     * 获胜用户Id（-100表示平局）
     */
    private Integer winUserId;

    /**
     * pk时间
     */
    private String pkTime;


    public List<JSONObject> getQuestionInfo() {
        return questionInfo;
    }

    public void setQuestionInfo(List<JSONObject> questionInfo) {
        this.questionInfo = questionInfo;
    }

    public Integer getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(Integer ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public Integer getPkUserId() {
        return pkUserId;
    }

    public void setPkUserId(Integer pkUserId) {
        this.pkUserId = pkUserId;
    }

    public String getOwnerHeadPic() {
        return ownerHeadPic;
    }

    public void setOwnerHeadPic(String ownerHeadPic) {
        this.ownerHeadPic = ownerHeadPic;
    }

    public String getPkHeadPic() {
        return pkHeadPic;
    }

    public void setPkHeadPic(String pkHeadPic) {
        this.pkHeadPic = pkHeadPic;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getPkName() {
        return pkName;
    }

    public void setPkName(String pkName) {
        this.pkName = pkName;
    }

    public Integer getOwnerScore() {
        return ownerScore;
    }

    public void setOwnerScore(Integer ownerScore) {
        this.ownerScore = ownerScore;
    }

    public Integer getPkScore() {
        return pkScore;
    }

    public void setPkScore(Integer pkScore) {
        this.pkScore = pkScore;
    }

    public Integer getOwnerRightCount() {
        return ownerRightCount;
    }

    public void setOwnerRightCount(Integer ownerRightCount) {
        this.ownerRightCount = ownerRightCount;
    }

    public Integer getPkRightCount() {
        return pkRightCount;
    }

    public void setPkRightCount(Integer pkRightCount) {
        this.pkRightCount = pkRightCount;
    }


    public String getOwnerDeptName() {
        return ownerDeptName;
    }

    public void setOwnerDeptName(String ownerDeptName) {
        this.ownerDeptName = ownerDeptName;
    }

    public String getPkDeptName() {
        return pkDeptName;
    }

    public void setPkDeptName(String pkDeptName) {
        this.pkDeptName = pkDeptName;
    }

    public Integer getWinUserId() {
        return winUserId;
    }

    public void setWinUserId(Integer winUserId) {
        this.winUserId = winUserId;
    }

    public String getPkTime() {
        return pkTime;
    }

    public void setPkTime(String pkTime) {
        this.pkTime = pkTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
