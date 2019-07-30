package com.enterprise.base.enums;

/**
 * 微应用使用状态枚举类
 *
 * @Author shisan
 * @Date 2018/3/26 上午10:56
 */
public enum UserStatusEnum {

    /**
     * 在职
     */
    ONJOB(1, "on_job","在职"),

    /**
     * 离职
     */
    LEAVEJOB(0, "leave_job","离职");

    private Integer value;

    private String desc;

    private String chiDesc;

    UserStatusEnum(Integer value, String desc, String chiDesc) {
        this.value = value;
        this.desc = desc;
        this.chiDesc = chiDesc;
    }

    public static UserStatusEnum getUserStatusEnum(Integer value) {
        for (UserStatusEnum userStatusEnum : UserStatusEnum.values()) {
            if (userStatusEnum.getValue().equals(value)) {
                return userStatusEnum;
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
