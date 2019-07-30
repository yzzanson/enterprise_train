package com.enterprise.base.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @Description 用户题库
 * @Author zezhouyang
 * @Date 18/4/3 上午10:08
 */
public class UserXLibraryVO2 {

    private Integer id;

    private String libraryName;

    private Integer totalCount;

//    private Integer schedule;
    private Integer answeredCount;

    private Integer remainFoodCount;

    private String lastAnswerTime;

    private Integer libraryId;

    private String title;

    private Integer titleType;

    //是否存在头衔
    private Integer titleFlag;

    //是否获得该头衔
    private Integer getFlag;

    //是否选择该头衔
    private Integer chooseFlag;

    private Integer isUpdate;

    private String label;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

//    public Integer getSchedule() {
//        return schedule;
//    }

//    public void setSchedule(Integer schedule) {
//        this.schedule = schedule;
//    }

    public String getLibraryName() {
        return libraryName;
    }

    public void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
    }

    public Integer getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(Integer libraryId) {
        this.libraryId = libraryId;
    }

    public Integer getAnsweredCount() {
        return answeredCount;
    }

    public void setAnsweredCount(Integer answeredCount) {
        this.answeredCount = answeredCount;
    }

    public String getLastAnswerTime() {
        return lastAnswerTime;
    }

    public void setLastAnswerTime(String lastAnswerTime) {
        this.lastAnswerTime = lastAnswerTime;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTitleFlag() {
        return titleFlag;
    }

    public void setTitleFlag(Integer titleFlag) {
        this.titleFlag = titleFlag;
    }

    public Integer getChooseFlag() {
        return chooseFlag;
    }

    public void setChooseFlag(Integer chooseFlag) {
        this.chooseFlag = chooseFlag;
    }

    public Integer getGetFlag() {
        return getFlag;
    }

    public void setGetFlag(Integer getFlag) {
        this.getFlag = getFlag;
    }


    public Integer getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(Integer isUpdate) {
        this.isUpdate = isUpdate;
    }

    public Integer getTitleType() {
        return titleType;
    }

    public void setTitleType(Integer titleType) {
        this.titleType = titleType;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getRemainFoodCount() {
        return remainFoodCount;
    }

    public void setRemainFoodCount(Integer remainFoodCount) {
        this.remainFoodCount = remainFoodCount;
    }

    public UserXLibraryVO2() {
    }

    public UserXLibraryVO2(Integer id, String libraryName,Integer libraryId, Integer isUpdate) {
        this.id = id;
        this.libraryName = libraryName;
        this.libraryId = libraryId;
        this.isUpdate = isUpdate;
    }

    public UserXLibraryVO2(Integer id, String libraryName,String label,Integer libraryId, Integer isUpdate) {
        this.id = id;
        this.libraryName = libraryName;
        this.label = label;
        this.libraryId = libraryId;
        this.isUpdate = isUpdate;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
