package com.enterprise.base.bean;

import com.dingtalk.open.client.api.model.corp.CorpUser;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * @Description
 * @Author zezhouyang
 * @Date 19/2/13 下午5:45
 */
public class DingUserDetail extends CorpUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private String avatar;
    private List<Long> department;
    private String dingId;
    private Boolean isBoss;
    private String order;
    private String unionid;

    public DingUserDetail() {
    }


    public Boolean getIsBoss() {
        return this.isBoss;
    }

    public void setIsBoss(Boolean isBoss) {
        this.isBoss = isBoss;
    }

    public String getDingId() {
        return this.dingId;
    }

    public void setDingId(String dingId) {
        this.dingId = dingId;
    }


    public List<Long> getDepartment() {
        return this.department;
    }

    public void setDepartment(List<Long> department) {
        this.department = department;
    }


    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public int hashCode() {
        boolean prime = true;
        int result = super.hashCode();
        result = 31 * result + (this.avatar == null ? 0 : this.avatar.hashCode());
        result = 31 * result + (this.department == null ? 0 : this.department.hashCode());
        result = 31 * result + (this.dingId == null ? 0 : this.dingId.hashCode());
        result = 31 * result + (this.isBoss == null ? 0 : this.isBoss.hashCode());
        result = 31 * result + (this.order == null ? 0 : this.order.hashCode());
        result = 31 * result + (this.unionid == null ? 0 : this.unionid.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!super.equals(obj)) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            DingUserDetail other = (DingUserDetail) obj;
            if (this.avatar == null) {
                if (other.avatar != null) {
                    return false;
                }
            } else if (!this.avatar.equals(other.avatar)) {
                return false;
            }

            if (this.department == null) {
                if (other.department != null) {
                    return false;
                }
            } else if (!this.department.equals(other.department)) {
                return false;
            }

            if (this.dingId == null) {
                if (other.dingId != null) {
                    return false;
                }
            } else if (!this.dingId.equals(other.dingId)) {
                return false;
            }

            if (this.isBoss == null) {
                if (other.isBoss != null) {
                    return false;
                }
            } else if (!this.isBoss.equals(other.isBoss)) {
                return false;
            }

            if (this.order == null) {
                if (other.order != null) {
                    return false;
                }
            } else if (!this.order.equals(other.order)) {
                return false;
            }

            if (this.unionid == null) {
                if (other.unionid != null) {
                    return false;
                }
            } else if (!this.unionid.equals(other.unionid)) {
                return false;
            }
            return true;
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }


}
