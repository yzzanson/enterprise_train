package com.enterprise.base.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description QuestionsEntity
 * @Author shisan
 * @Date 2018/3/23 上午10:43
 */
public class QuestionsEntity implements Serializable {

    private static final long serialVersionUID = 526324944925280489L;

    /**
     * 自增主键
     */
    private Integer id;

    /**
     * 所属题库Id
     */
    private Integer libraryId;

    /**题目创建人*/
    private Integer operator;

    /**
     * 题目类型,QuestionTypeEnum
     */
    private Integer type;


    private Integer labelId;

    /**
     * 题目描述
     */
    private String description;

    /**
     * 题目选项({"A":"","B":"",...})
     */
    private String options;

    /**
     * 题目正确选项(多个用,隔开)
     */
    private String answer;

    /**
     * 题目正确选项(多个用,隔开)
     */
    private String answerDesc;

    /**
     * 0可以不按顺序 1按顺序
     * */
    private Integer blankIndex;

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

    /**
     * 是否重新学习
     * */
    private Integer restudy;

    /**
     * 对应默认题库中的questionId,用于统计学习次数等
     * */
    private Integer parentId;


    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setLibraryId(Integer libraryId) {
        this.libraryId = libraryId;
    }

    public Integer getLibraryId() {
        return libraryId;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getOptions() {
        return options;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswerDesc(String answerDesc) {
        this.answerDesc = answerDesc;
    }

    public String getAnswerDesc() {
        return answerDesc;
    }

    public Integer getBlankIndex() {
        return blankIndex;
    }

    public void setBlankIndex(Integer blankIndex) {
        this.blankIndex = blankIndex;
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

    public Integer getOperator() {
        return operator;
    }

    public void setOperator(Integer operator) {
        this.operator = operator;
    }

    public Integer getRestudy() {
        return restudy;
    }

    public void setRestudy(Integer restudy) {
        this.restudy = restudy;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getLabelId() {
        return labelId;
    }

    public void setLabelId(Integer labelId) {
        this.labelId = labelId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
