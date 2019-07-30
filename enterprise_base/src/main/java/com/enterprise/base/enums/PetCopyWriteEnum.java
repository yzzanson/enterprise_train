package com.enterprise.base.enums;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/4/28 下午6:53
 */
public enum PetCopyWriteEnum {

    /**
     * 随机文案
     */
    RANDOM(0, "random","随机文案"),

    /**
     * 1天以上没登录
     */
    NOTLOGIN(1, "nologin","1天以上没登录"),

    /**
     * 部门pk后,赢了

    DEPT_PK_WIN(2, "deptPKWin","部门pk胜利"),
     */
    /**
     * 部门pk后,输了

    DEPT_PK_LOSE(3, "deptPKLose","部门pk输了"),
     */
    /**
     * 个人pk,赢了

    SINGLE_PK_WIN(4, "singlePKWin","个人pk胜利"),
     */
    /**
     * 部门pk后,输了

    SINGLE_PK_LOSE(5, "singlePKLose","个人pk输了"),
     */
    /**
     * 部门pk后,输了
     */
    PET_HUNGER(6, "petHunger","宠物饿了"),
    ;

    private Integer value;

    private String desc;

    private String chiDesc;

    PetCopyWriteEnum(Integer value, String desc,String chiDesc) {
        this.value = value;
        this.desc = desc;
        this.chiDesc = chiDesc;
    }

    public static AgentStatusEnum getPetCopyWriteEnum(Integer value) {
        for (AgentStatusEnum agentStatusEnum : AgentStatusEnum.values()) {
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
