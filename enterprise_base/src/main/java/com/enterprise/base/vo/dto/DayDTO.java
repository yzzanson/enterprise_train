package com.enterprise.base.vo.dto;

import java.util.Date;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/4/14 下午11:54
 */
public class DayDTO {

    private Date startDate;

    private Date endDate;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public DayDTO() {
    }

    public DayDTO(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
