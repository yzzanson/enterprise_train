package com.enterprise.base.enums;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/10/26 上午10:35
 */
public enum DynamicEnum {

    /**
     * 封印咒
     */
    SEAL(1, "seal", "封印咒","小猫被%s封印了"),

    /**
     * 小衰神
     */
    DECAY(2, "decay", "小衰神","小猫被%s的小衰神附体了，附体期间答题无法获得猫粮哦"),

    /**
     * 爆裂拳
     */
    Dynamicpunch(3, "dynamicpunch", "爆裂拳","小猫被%s狂扁了"),
    /**
     * 丘比特
     */
    CUPID(4, "cupid", "丘比特","%s给你发射了爱神之箭~一方答题另一方获得相同猫粮哦！累计帮我赚得%sg猫粮"),

    /**
     * 回魂丹
     * */
    RESURRECT(7, "resurrect", "回魂丹",""),

    /**
     * 生长液
     * */
    GROWTH_LIQUID(8, "resurrect", "生长液","%s对小猫使用了生长液，小猫得到%sg爱心"),

    /**
     * 其他人清扫
     * */
    OTHER_CLEAN(9, "otherclean", "其他人清扫","%s帮小猫打扫了卫生"),

    /**
     * 自己清扫
     * */
    SELF_CLEAN(10, "selfclean", "自己清扫","你给小猫打扫获得%sg猫粮"),

    /**
     * 投食
     * */
    OTHER_FEED(11, "otherfeed", "投食","%s给小猫投喂了%sg猫粮"),

    /**
     * 串门
     * */
    VISIT(12, "visit", "串门","%s来了一下");

    private Integer value;

    private String desc;

    private String chiDesc;

    private String dynamic;

    DynamicEnum(Integer value, String desc,String chiDesc,String dynamic) {
        this.value = value;
        this.desc = desc;
        this.chiDesc = chiDesc;
        this.dynamic = dynamic;
    }

    public static DynamicEnum getDynamicEnum(Integer value) {
        for (DynamicEnum dynamicEnum : DynamicEnum.values()) {
            if (dynamicEnum.getValue().equals(value)) {
                return dynamicEnum;
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

    public String getDynamic() {
        return dynamic;
    }

    public void setDynamic(String dynamic) {
        this.dynamic = dynamic;
    }


}
