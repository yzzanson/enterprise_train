package com.enterprise.base.vo;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/7/4 下午3:41
 */
public class DepartmentVO {

    private Integer id;

    private String name;

    private Integer parentId;

    private Integer hasChild;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getHasChild() {
        return hasChild;
    }

    public void setHasChild(Integer hasChild) {
        this.hasChild = hasChild;
    }

    public DepartmentVO(Integer id, String name, Integer parentId) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
    }

    public DepartmentVO() {
    }
}
