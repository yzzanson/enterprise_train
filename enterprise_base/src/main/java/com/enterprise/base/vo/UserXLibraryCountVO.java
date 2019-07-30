package com.enterprise.base.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 用户-题库关系
 * Created by anson on 18/3/27.
 */
public class UserXLibraryCountVO {

    private Integer id;

    private String name;

    private String deptName;

    private String schedule;

    private String accuracy;

    private String finishTime;

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

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public UserXLibraryCountVO() {
    }

    public UserXLibraryCountVO(String name, String deptName, String schedule, String accuracy, String finishTime) {
        this.name = name;
        this.deptName = deptName;
        this.schedule = schedule;
        this.accuracy = accuracy;
        this.finishTime = finishTime;
    }

    public UserXLibraryCountVO(String name, String deptName, String schedule, String finishTime) {
        this.name = name;
        this.deptName = deptName;
        this.schedule = schedule;
        this.finishTime = finishTime;
    }

    public UserXLibraryCountVO(Integer id, String name, String deptName, String schedule, String accuracy, String finishTime) {
        this.id = id;
        this.name = name;
        this.deptName = deptName;
        this.schedule = schedule;
        this.accuracy = accuracy;
        this.finishTime = finishTime;
    }

    public UserXLibraryCountVO(Integer id, String name, String deptName, String schedule, String finishTime) {
        this.id = id;
        this.name = name;
        this.deptName = deptName;
        this.schedule = schedule;
        this.finishTime = finishTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
