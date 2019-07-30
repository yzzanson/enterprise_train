package com.enterprise.service.user;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.entity.UserXDeptEntity;

import java.util.List;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/4/16 上午10:03
 */
public interface UserXDeptService {

    Integer getDepartmentId(String corpId, Integer userId);

    UserXDeptEntity getUserXDeptByCorpIdAndDeptId(UserXDeptEntity userXDeptEntity);

    void createUserXDept(UserXDeptEntity userXDeptEntity);

    List<UserXDeptEntity> getUserDepts(String corpId, Integer userId,Integer type);

    void batchSafeDelte(UserXDeptEntity userXDeptEntity);

    UserXDeptEntity getByUserIdAndCorpIdAndDeptId(UserXDeptEntity userXDeptEntity);

    JSONObject getMyDeptList(String corpId,Integer userId);
}
