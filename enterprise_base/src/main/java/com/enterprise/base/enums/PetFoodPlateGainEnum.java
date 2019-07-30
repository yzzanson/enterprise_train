package com.enterprise.base.enums;

/**
 * @Description 宠物获取饲料方式
 * @Author zezhouyang
 * @Date 18/10/22 下午2:43
 */
public enum PetFoodPlateGainEnum {


    /**
     * 自己喂食
     */
    SELF_FEED(1, "self_feed","自己喂食"),

    /**
     * 别人投喂
     */
    OTHER_FEED(2, "other_feed","其他人投喂"),

    /**
     * 自动消耗
     */
    AUTO_CONSUME(3, "auto_consume","自动消耗");

    private Integer value;

    private String desc;

    private String chiDesc;

    PetFoodPlateGainEnum(Integer value, String desc, String chiDesc) {
        this.value = value;
        this.desc = desc;
        this.chiDesc = chiDesc;
    }

    public static PetFoodPlateGainEnum getPetFoodGainEnum(Integer value) {
        for (PetFoodPlateGainEnum petFoodGainEnum : PetFoodPlateGainEnum.values()) {
            if (petFoodGainEnum.getValue().equals(value)) {
                return petFoodGainEnum;
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
