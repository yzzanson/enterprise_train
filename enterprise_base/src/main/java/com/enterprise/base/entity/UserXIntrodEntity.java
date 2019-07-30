package com.enterprise.base.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

/**
 * @Description 用户引导
 * @Author zezhouyang
 * @Date 18/6/28 下午5:33
 */
public class UserXIntrodEntity {

    private Integer id;

    private Integer userId;

    //选择题库
    private Integer growthAnswer;

    //pk
    private Integer chooseLib;

    //挑衅
    private Integer wrongAnswer;

    //擂台
    private Integer arena;

    //初始化擂台
    private Integer arenaInit;

    //擂台时间
    private Integer arenaTime;

    //选择战队
    private Integer chooseTeam;

    //挑衅
    private Integer challenge;

    //@1.3.0背包
    private Integer bag;

    private Integer newVersion;

    //Version1.4.0
    //喂食
    private Integer feedPet;
    //投食
    private Integer otherFeedPet;
    //清扫
    private Integer cleanPet;
    //答题
    private Integer answer;

    private Date createTime;

    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getGrowthAnswer() {
        return growthAnswer;
    }

    public void setGrowthAnswer(Integer growthAnswer) {
        this.growthAnswer = growthAnswer;
    }

    public Integer getChooseLib() {
        return chooseLib;
    }

    public void setChooseLib(Integer chooseLib) {
        this.chooseLib = chooseLib;
    }

    public Integer getWrongAnswer() {
        return wrongAnswer;
    }

    public void setWrongAnswer(Integer wrongAnswer) {
        this.wrongAnswer = wrongAnswer;
    }

    public Integer getArena() {
        return arena;
    }

    public void setArena(Integer arena) {
        this.arena = arena;
    }

    public Integer getArenaInit() {
        return arenaInit;
    }

    public void setArenaInit(Integer arenaInit) {
        this.arenaInit = arenaInit;
    }

    public Integer getArenaTime() {
        return arenaTime;
    }

    public void setArenaTime(Integer arenaTime) {
        this.arenaTime = arenaTime;
    }

    public Integer getChooseTeam() {
        return chooseTeam;
    }

    public void setChooseTeam(Integer chooseTeam) {
        this.chooseTeam = chooseTeam;
    }

    public Integer getChallenge() {
        return challenge;
    }

    public void setChallenge(Integer challenge) {
        this.challenge = challenge;
    }

    public Integer getNewVersion() {
        return newVersion;
    }

    public void setNewVersion(Integer newVersion) {
        this.newVersion = newVersion;
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

    public Integer getBag() {
        return bag;
    }

    public void setBag(Integer bag) {
        this.bag = bag;
    }

    public Integer getFeedPet() {
        return feedPet;
    }

    public void setFeedPet(Integer feedPet) {
        this.feedPet = feedPet;
    }

    public Integer getOtherFeedPet() {
        return otherFeedPet;
    }

    public void setOtherFeedPet(Integer otherFeedPet) {
        this.otherFeedPet = otherFeedPet;
    }

    public Integer getCleanPet() {
        return cleanPet;
    }

    public void setCleanPet(Integer cleanPet) {
        this.cleanPet = cleanPet;
    }

    public Integer getAnswer() {
        return answer;
    }

    public void setAnswer(Integer answer) {
        this.answer = answer;
    }

    public UserXIntrodEntity() {
    }

    public UserXIntrodEntity(Integer userId, Integer growthAnswer, Integer chooseLib, Integer wrongAnswer, Integer arena, Integer arenaInit, Integer arenaTime, Integer chooseTeam, Integer challenge, Date createTime, Date updateTime) {
        this.userId = userId;
        this.growthAnswer = growthAnswer;
        this.chooseLib = chooseLib;
        this.wrongAnswer = wrongAnswer;
        this.arena = arena;
        this.arenaInit = arenaInit;
        this.arenaTime = arenaTime;
        this.chooseTeam = chooseTeam;
        this.challenge = challenge;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

//    public UserXIntrodEntity(Integer id, Integer userId, Integer growthAnswer, Integer chooseLib, Integer wrongAnswer, Integer arena, Integer arenaInit, Integer arenaTime, Integer chooseTeam, Integer challenge) {
//        this.id = id;
//        this.userId = userId;
//        this.growthAnswer = growthAnswer;
//        this.chooseLib = chooseLib;
//        this.wrongAnswer = wrongAnswer;
//        this.arena = arena;
//        this.arenaInit = arenaInit;
//        this.arenaTime = arenaTime;
//        this.chooseTeam = chooseTeam;
//        this.challenge = challenge;
//    }
//
//    public UserXIntrodEntity(Integer userId, Integer growthAnswer, Integer chooseLib, Integer wrongAnswer, Integer arena, Integer arenaInit, Integer arenaTime, Integer chooseTeam, Integer challenge) {
//        this.userId = userId;
//        this.growthAnswer = growthAnswer;
//        this.chooseLib = chooseLib;
//        this.wrongAnswer = wrongAnswer;
//        this.arena = arena;
//        this.arenaInit = arenaInit;
//        this.arenaTime = arenaTime;
//        this.chooseTeam = chooseTeam;
//        this.challenge = challenge;
//    }


//    public UserXIntrodEntity(Integer id, Integer userId, Integer growthAnswer, Integer chooseLib, Integer wrongAnswer, Integer arena, Integer arenaInit, Integer arenaTime, Integer chooseTeam, Integer challenge, Integer bag) {
//        this.id = id;
//        this.userId = userId;
//        this.growthAnswer = growthAnswer;
//        this.chooseLib = chooseLib;
//        this.wrongAnswer = wrongAnswer;
//        this.arena = arena;
//        this.arenaInit = arenaInit;
//        this.arenaTime = arenaTime;
//        this.chooseTeam = chooseTeam;
//        this.challenge = challenge;
//        this.bag = bag;
//    }

    public UserXIntrodEntity(Integer id,Integer userId, Integer growthAnswer, Integer chooseLib, Integer wrongAnswer, Integer arena, Integer arenaInit, Integer arenaTime, Integer chooseTeam, Integer challenge, Integer bag, Integer newVersion, Integer feedPet, Integer otherFeedPet, Integer cleanPet, Integer answer) {
        this.id = id;
        this.userId = userId;
        this.growthAnswer = growthAnswer;
        this.chooseLib = chooseLib;
        this.wrongAnswer = wrongAnswer;
        this.arena = arena;
        this.arenaInit = arenaInit;
        this.arenaTime = arenaTime;
        this.chooseTeam = chooseTeam;
        this.challenge = challenge;
        this.bag = bag;
        this.newVersion = newVersion;
        this.feedPet = feedPet;
        this.otherFeedPet = otherFeedPet;
        this.cleanPet = cleanPet;
        this.answer = answer;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }


}
