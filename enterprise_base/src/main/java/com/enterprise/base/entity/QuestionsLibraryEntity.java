package com.enterprise.base.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by null on 2018-03-23 10:46:42
 */
public class QuestionsLibraryEntity implements Serializable {

    private static final long serialVersionUID = 523324945925287489L;

    /**
     * 自增主键
     */
    private Integer id;

    /**
     * 公司id
     */
    private Integer companyId;

    /**
     * 题库名称
     */
    private String name;

    /**
     * 使用次数
     */
    private Integer useCount;

    /**
     * 题库分类(0:公共题库(对应题库模板), 1:私有题库(对应自定义题库) , 2:官方题库
     */
    private Integer subject;

    /**
     * 标签
     */
    private String label;

    /**
     * 题库创建人
     */
    private Integer operator;

    /**
     * 状态(0:删除，1:正常,2:停用)
     */
    private Integer status;

    //父id,如果企业使用了官方题库则会增加
    private Integer parentId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否为默认题库
     * */
    private Integer defaultFlag;

    /**
     * 排序规则 0/1 随机/顺序
     * */
    private Integer sortType;

    /**
     * 答案顺序 0/1 随机/顺序
     * */
    private Integer optionSortType;

    /**
     * 是否通知 0/1 不通知/通知
     * */
    private Integer isOA;

    /**
     * 题库题目总数
     * */
    private Integer totalCount;

    @Transient
    private List<String> labelList;

    @Transient
    private Integer companyLibraryId;

    /**
     * 头衔
     * */
    @Transient
    private String title;

    @Transient
    private Integer titleType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getUseCount() {
        return useCount;
    }

    public void setUseCount(Integer useCount) {
        this.useCount = useCount;
    }

    public Integer getSubject() {
        return subject;
    }

    public void setSubject(Integer subject) {
        this.subject = subject;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getOperator() {
        return operator;
    }

    public void setOperator(Integer operator) {
        this.operator = operator;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
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

    public Integer getDefaultFlag() {
        return defaultFlag;
    }

    public void setDefaultFlag(Integer defaultFlag) {
        this.defaultFlag = defaultFlag;
    }

    public Integer getSortType() {
        return sortType;
    }

    public void setSortType(Integer sortType) {
        this.sortType = sortType;
    }

    public Integer getOptionSortType() {
        return optionSortType;
    }

    public void setOptionSortType(Integer optionSortType) {
        this.optionSortType = optionSortType;
    }

    public Integer getIsOA() {
        return isOA;
    }

    public void setIsOA(Integer isOA) {
        this.isOA = isOA;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public List<String> getLabelList() {
        return labelList;
    }

    public void setLabelList(List<String> labelList) {
        this.labelList = labelList;
    }

    public Integer getCompanyLibraryId() {
        return companyLibraryId;
    }

    public void setCompanyLibraryId(Integer companyLibraryId) {
        this.companyLibraryId = companyLibraryId;
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

    public QuestionsLibraryEntity() {
    }

    public QuestionsLibraryEntity(Integer id) {
        this.id = id;
    }

    public QuestionsLibraryEntity(String name, Integer subject, Integer status) {
        this.name = name;
        this.subject = subject;
        this.status = status;
    }

    public QuestionsLibraryEntity(Integer status, Integer subject, String name, Integer companyId) {
        this.status = status;
        this.subject = subject;
        this.name = name;
        this.companyId = companyId;
    }

    public QuestionsLibraryEntity(Integer companyId, Integer subject, Integer status, Integer defaultFlag) {
        this.companyId = companyId;
        this.subject = subject;
        this.status = status;
        this.defaultFlag = defaultFlag;
    }

    public QuestionsLibraryEntity(Integer companyId, Integer parentId) {
        this.companyId = companyId;
        this.parentId = parentId;
    }

    public QuestionsLibraryEntity(Integer companyId, Integer parentId,Integer operator) {
        this.companyId = companyId;
        this.parentId = parentId;
        this.operator = operator;
    }

    public QuestionsLibraryEntity(Integer companyId, String name, Integer useCount, Integer subject, String label, Integer operator, Integer status, Integer parentId, Date createTime, Date updateTime, Integer defaultFlag) {
        this.companyId = companyId;
        this.name = name;
        this.useCount = useCount;
        this.subject = subject;
        this.label = label;
        this.operator = operator;
        this.status = status;
        this.parentId = parentId;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.defaultFlag = defaultFlag;
    }

    public QuestionsLibraryEntity(Integer companyId, String name, Integer useCount, Integer subject, String label, Integer operator, Integer status, Integer parentId, Date createTime, Date updateTime, Integer defaultFlag, Integer sortType) {
        this.companyId = companyId;
        this.name = name;
        this.useCount = useCount;
        this.subject = subject;
        this.label = label;
        this.operator = operator;
        this.status = status;
        this.parentId = parentId;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.defaultFlag = defaultFlag;
        this.sortType = sortType;
    }

    public QuestionsLibraryEntity(Integer id, Date updateTime) {
        this.id = id;
        this.updateTime = updateTime;
    }

    public QuestionsLibraryEntity(Integer id, Integer totalCount, Date updateTime) {
        this.id = id;
        this.totalCount = totalCount;
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
