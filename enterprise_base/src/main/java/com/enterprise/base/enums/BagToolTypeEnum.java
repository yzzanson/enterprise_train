package com.enterprise.base.enums;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/9/6 下午1:37
 */
public enum BagToolTypeEnum {


    /**
     * 封印咒
     */
    SEAL(1, "seal", "封印咒"),

    /**
     * 小衰神
     */
    DECAY(2, "decay", "小衰神"),

    /**
     * 爆裂拳
     */
    Dynamicpunch(3, "dynamicpunch", "爆裂拳"),
    /**
     * 丘比特
     */
    CUPID(4, "cupid", "丘比特"),
    /**
     * 马赛克
     */
    MOSAIC(5, "mosaic", "马赛克"),
    /**
     * 窜天猴
     */
//    RHESUS(6, "rhesus", "窜天炮"),
    RHESUS(6, "rhesus", "窜天炮"),

    /**
     * 回魂丹
     * */
    RESURRECT(7, "resurrect", "回魂丹"),

    /**
     * 生长液
     * */
    GROWTH_LIQUID(8, "resurrect", "生长液")
    ;

    private Integer value;

    private String desc;

    private String chiDesc;


    BagToolTypeEnum(Integer value, String desc, String chiDesc) {
        this.value = value;
        this.desc = desc;
        this.chiDesc = chiDesc;
    }

    public static BagToolTypeEnum getAgentStatusEnum(Integer value) {
        for (BagToolTypeEnum bagToolGainTypeEnum : BagToolTypeEnum.values()) {
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

}