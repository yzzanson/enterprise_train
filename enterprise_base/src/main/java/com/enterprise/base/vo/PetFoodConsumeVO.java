package com.enterprise.base.vo;

import java.util.Date;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/10/24 下午4:08
 */
public class PetFoodConsumeVO {

    private Integer result;

    private Integer consumeTime;

    private Date nextConsumeTime;

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public Integer getConsumeTime() {
        return consumeTime;
    }

    public void setConsumeTime(Integer consumeTime) {
        this.consumeTime = consumeTime;
    }

    public Date getNextConsumeTime() {
        return nextConsumeTime;
    }

    public void setNextConsumeTime(Date nextConsumeTime) {
        this.nextConsumeTime = nextConsumeTime;
    }

    public PetFoodConsumeVO() {
    }

    public PetFoodConsumeVO(Integer result, Date nextConsumeTime) {
        this.result = result;
        this.nextConsumeTime = nextConsumeTime;
    }
}
