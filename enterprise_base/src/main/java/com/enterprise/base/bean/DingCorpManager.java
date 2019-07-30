package com.enterprise.base.bean;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/7/2 上午11:21
 */
public class DingCorpManager {

    private static final long serialVersionUID = 8319848715636818658L;
    private Long sys_level;
    private String userid;

    public Long getSys_level() {
        return sys_level;
    }

    public void setSys_level(Long sys_level) {
        this.sys_level = sys_level;
    }

    public String getUserid() {
        return this.userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
