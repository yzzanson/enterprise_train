package com.enterprise.base.vo.studyRemind;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/4/23 下午4:00
 */
public class StudyRemindVO {

    private Integer id;

    private Integer deptid;

    private Integer userid;

    private String deptname;

    private String username;

    private Integer libraryId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDeptid() {
        return deptid;
    }

    public void setDeptid(Integer deptid) {
        this.deptid = deptid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getDeptname() {
        return deptname;
    }

    public void setDeptname(String deptname) {
        this.deptname = deptname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(Integer libraryId) {
        this.libraryId = libraryId;
    }

    public StudyRemindVO() {
    }

    public StudyRemindVO(Integer deptid, String deptname) {
        this.deptid = deptid;
        this.deptname = deptname;
    }


}
