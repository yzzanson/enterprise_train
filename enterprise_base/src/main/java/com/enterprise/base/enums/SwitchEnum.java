package com.enterprise.base.enums;

/**
 * 微应用使用状态枚举类
 *
 * @Author shisan
 * @Date 2018/3/26 上午10:56
 */
public enum SwitchEnum {

    /**
     * 喂养自己
     */
    FEED_MY_PET(1, "feed_my_pet","喂养自己的宠物"),

    /**
     * 喂养其他人
     */
    FEED_OTEHER(2, "feed_other","喂养其他"),

    /**
     * 产生垃圾
     */
    GEN_PAPER_BALL(3, "gen_paper_ball","产生垃圾"),

    /**
     * 消耗饲料
     */
    CONSUME_FOOD(4, "consume_food","消耗食物")
    ;

    private Integer value;

    private String desc;

    private String chiDesc;

    SwitchEnum(Integer value, String desc, String chiDesc) {
        this.value = value;
        this.desc = desc;
        this.chiDesc = chiDesc;
    }

    public static SwitchEnum getAgentStatusEnum(Integer value) {
        for (SwitchEnum agentStatusEnum : SwitchEnum.values()) {
            if (agentStatusEnum.getValue().equals(value)) {
                return agentStatusEnum;
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
