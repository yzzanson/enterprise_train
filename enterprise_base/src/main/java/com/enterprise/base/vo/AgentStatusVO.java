package com.enterprise.base.vo;

import com.enterprise.base.enums.AgentStatusEnum;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author zezhouyang
 * @Date 19/1/18 下午2:24
 */
public class AgentStatusVO {

    private Integer type;

    private String desc;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public AgentStatusVO(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public AgentStatusVO() {
    }


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public static List<AgentStatusVO> getAgentStatusList(){
        List<AgentStatusVO> resultList = new ArrayList<>();
        for (AgentStatusEnum agentStatusEnum : AgentStatusEnum.values()) {
            Integer type = agentStatusEnum.getValue();
            String value = agentStatusEnum.getChiDesc();
            AgentStatusVO agentStatusVO = new AgentStatusVO(type,value);
            resultList.add(agentStatusVO);
        }
        return resultList;
    }

}
