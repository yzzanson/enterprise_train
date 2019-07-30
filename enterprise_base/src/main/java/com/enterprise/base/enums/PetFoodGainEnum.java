package com.enterprise.base.enums;

/**
 * @Description 宠物获取饲料方式
 * @Author zezhouyang
 * @Date 18/10/22 下午2:43
 */
public enum PetFoodGainEnum {


    /**
     * 初始化
     */
    INIT(1, "init","初始化"),

    /**
     * 答题
     */
    ANSWER(2, "answer","答题"),


    /**
     * 喂食
     */
    FEED(3, "feed","喂食"),


    /**
     * 清扫
     */
    CLEAN(4, "clean","清扫"),

    /**
     * 喂食他人
     */
    FEEDOTHER(5, "feedOther","喂食他人"),


    /**
     * 奇比特对方答题
     */
    CUPIDANSWER(6, "cupidAnswer","丘比特他人答题");

    private Integer value;

    private String desc;

    private String chiDesc;

    PetFoodGainEnum(Integer value, String desc,String chiDesc) {
        this.value = value;
        this.desc = desc;
        this.chiDesc = chiDesc;
    }

    public static PetFoodGainEnum getPetFoodGainEnum(Integer value) {
        for (PetFoodGainEnum petFoodGainEnum : PetFoodGainEnum.values()) {
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
