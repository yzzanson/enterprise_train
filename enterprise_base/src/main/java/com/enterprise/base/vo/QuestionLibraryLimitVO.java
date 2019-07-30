package com.enterprise.base.vo;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.ResultJson;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @Description
 * @Author zezhouyang
 * @Date 19/3/11 下午5:18
 */
public class QuestionLibraryLimitVO {

    private String version;

    private Integer libraryCount;

    private String limitCount;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getLibraryCount() {
        return libraryCount;
    }

    public void setLibraryCount(Integer libraryCount) {
        this.libraryCount = libraryCount;
    }

    public String getLimitCount() {
        return limitCount;
    }

    public void setLimitCount(String limitCount) {
        this.limitCount = limitCount;
    }

    public QuestionLibraryLimitVO() {
    }


    public QuestionLibraryLimitVO(String version, Integer libraryCount, String limitCount) {
        this.version = version;
        this.libraryCount = libraryCount;
        this.limitCount = limitCount;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public static void main(String[] args) {
        QuestionLibraryLimitVO questionLibraryLimitVO = new QuestionLibraryLimitVO("1-200人版",11,"10");
        JSONObject resultJson = ResultJson.errorResultJson(questionLibraryLimitVO);
        System.out.println(resultJson.toJSONString());
    }
}
