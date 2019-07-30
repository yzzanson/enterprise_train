package com.enterprise.base.enums;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/9/6 下午1:37
 */
public enum BagToolGainTypeEnum {


    /**
     * 登陆
     */
    LOGIN(1, "login","登陆",0),

    /**
     * 答对4题
     */
    CORRECT_FOUR(2, "correct_four","累计答对4题",4),

    /**
     * 答对12题
     */
    CORRECT_TWELVE(3, "correct_twelve","累计答对12题",12),
    /**
     * 答对24题
     */
    CORRECT_TWENTYFOUR(4, "correct_twentyfour","累计答对24题",24),

    /**
     * 答对36题
     */
    CORRECT_THIRTYSIX(5, "correct_thirtysix","累计答对36题",36),

    /**
     * 答对48题
     */
    CORRECT_FOURTYEIGHT(6, "correct_fourtyeight","累计答对48题",48),


    /**
     * 答对60题
     */
    CORRECT_SIXTY(7, "correct_sixty","累计答对60题",60),

    /**
     * 连续答对3题
     */
    COMBO_THREE(8, "combo_three","COMBO3",0),
    /**
     * 连续答对5题
     */
    COMBO_FIVE(9, "combo_five","COMBO5",0),

    /**
     * 连续答对7题
     */
    COMBO_SEVEN(10, "combo_five","COMBO7",0),

    /**
     * 连续答对9题
     */
    COMBO_NINE(11, "combo_five","COMBO9",0);

    private Integer value;

    private String desc;

    private String chiDesc;

    private Integer count;

    BagToolGainTypeEnum(Integer value, String desc, String chiDesc, Integer count) {
        this.value = value;
        this.desc = desc;
        this.chiDesc = chiDesc;
        this.count = count;
    }

    public static BagToolGainTypeEnum getAgentStatusEnum(Integer value) {
        for (BagToolGainTypeEnum bagToolGainTypeEnum : BagToolGainTypeEnum.values()) {
            if (bagToolGainTypeEnum.getValue().equals(value)) {
                return bagToolGainTypeEnum;
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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
