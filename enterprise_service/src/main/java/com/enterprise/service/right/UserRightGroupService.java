package com.enterprise.service.right;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.entity.right.UserRightGroupEntity;

import java.util.List;

/**
 * Created by anson on 18/3/24.
 */
public interface UserRightGroupService {

    List<UserRightGroupEntity> findUserRightGroups(Integer companyId,Integer userId);

    UserRightGroupEntity findUserRightGroupByGroupId(UserRightGroupEntity userRightGroupEntity);

    Integer getUserRightGroupCount(UserRightGroupEntity userRightGroupEntity);

    Integer getUserGroupByCompanyIdAndUserId(Integer companyId,Integer userId);

    Integer batchInsert(List<UserRightGroupEntity> insertList);

    Integer batchUpdate(List<UserRightGroupEntity> updateList);

    /**
     * 保存用户权限组
     * */
    JSONObject saveUserRightGroup(String userIds,Integer groupId);

    /**
     * 获取用户菜单列表
     * */
    JSONObject getUserResource();

    /**
     * 获取用户菜单列表
     * */
    JSONObject getUserResource2(String corpId,Integer companyId,Integer userId);

    /**
     * 判断是否是超管
     * */
    Integer checkIsSuperManage(Integer companyId,Integer userId);


}
