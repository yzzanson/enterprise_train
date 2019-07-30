package com.enterprise.base.vo.bagtool;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/9/6 上午10:33
 */
public class BagToolDescVO {

    private String playDesc;

    private String gainTool;

    private List<BagToolVO> toolList;


    public String getPlayDesc() {
        return playDesc;
    }

    public void setPlayDesc(String playDesc) {
        this.playDesc = playDesc;
    }

    public String getGainTool() {
        return gainTool;
    }

    public void setGainTool(String gainTool) {
        this.gainTool = gainTool;
    }

    public List<BagToolVO> getToolList() {
        return toolList;
    }

    public void setToolList(List<BagToolVO> toolList) {
        this.toolList = toolList;
    }


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
