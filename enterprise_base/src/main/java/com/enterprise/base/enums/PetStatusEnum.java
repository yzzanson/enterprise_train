package com.enterprise.base.enums;

/**
 * Created by anson on 18/3/24.
 */
public enum PetStatusEnum {

    /**
     * 都未提醒
     */
    ALL_NO_REMIND(0, "all_no_remind"),

    /**
     * 擂主提醒
     */
    OWNER_REMIND(1, "owner_remind"),

    /**
     * 挑战者提醒
     */
    CHALL_REMIND(2,"chall_remind"),

    /**
     * 都提醒
     */
    BOTH_REMIND(3,"both_remind");

    private Integer value;

    private String desc;

    PetStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
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


}
