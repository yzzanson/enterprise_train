package com.enterprise.base.enums;

/**
 * 微应用使用状态枚举类
 *
 * @Author shisan
 * @Date 2018/3/26 上午10:56
 */
public enum MarketVersionEnum {

    /**
     * 1-200人
     */
//    TWO_HUNDRED(10, "two_hundred","1-200人"),
    TWO_HUNDRED(0, "two_hundred","1-200人"),

    /**
     * 免费
     */
    FREE(0, "free","免费版"),

    /**
     * 试用
     */
    TRIAL(1, "trial","试用规格"),

    /**
     * 1-49人
     */
//    FORTY_NINE(10, "forty_nine","1-49人"),
    FORTY_NINE(0, "forty_nine","1-49人"),

    /**
     * 高级版
     */
    ADVANCED(0, "advanced","高级版"),

    /**
     * 基础版
     */
    BASE(10, "base","基础版"),

    /**
     * 20-99人
     */
//    TWENTY_NINTYNIGHT(10, "twenty_nintynight","20-99人");
    TWENTY_NINTYNIGHT(0, "twenty_nintynight","20-99人");

    private Integer value;

    private String desc;

    private String chiDesc;

    MarketVersionEnum(Integer value, String desc, String chiDesc) {
        this.value = value;
        this.desc = desc;
        this.chiDesc = chiDesc;
    }

    public static MarketVersionEnum getAgentStatusEnum(Integer value) {
        for (MarketVersionEnum marketVersionEnum : MarketVersionEnum.values()) {
            if (marketVersionEnum.getValue().equals(value)) {
                return marketVersionEnum;
            }
        }

        return null;
    }

    public static MarketVersionEnum getAgentStatusEnum(String value) {
        for (MarketVersionEnum marketVersionEnum : MarketVersionEnum.values()) {
            if (marketVersionEnum.getChiDesc().equals(value) || value.contains(marketVersionEnum.getChiDesc())) {
                return marketVersionEnum;
            }
        }

        return null;
    }


    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getChiDesc() {
        return chiDesc;
    }

    public void setChiDesc(String chiDesc) {
        this.chiDesc = chiDesc;
    }


    public static void main(String[] args) {
        MarketVersionEnum maketVersion = getAgentStatusEnum("基础版20人");
        System.out.println(maketVersion.getDesc());
    }
}
