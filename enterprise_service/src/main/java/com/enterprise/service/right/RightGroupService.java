package com.enterprise.service.right;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.PageEntity;
import com.enterprise.base.entity.right.RightGroupEntity;

import java.util.List;

/**
 * Created by anson on 18/3/23.
 */
public interface RightGroupService {

    Integer saveOrUpdateRightGroup(RightGroupEntity rightGroupEntity);
    Integer saveOrUpdateRightGroupSingle(RightGroupEntity rightGroupEntity);

    List<RightGroupEntity> getByCompanyIdAndName(Integer companyId,Integer status,String name);

    JSONObject getRightGroups(Integer companyId,PageEntity pageEntity);

    JSONObject getDepartmentGroup(Integer deptId,Integer rightGroupId,String name)  throws Exception;

    JSONObject getGroupUser(Integer rightGroupId);

    /**
     * 初始化公司超管权限组
     * */
    Integer initCompanyManageGroup(String corpId);

    JSONObject findUserRightGroup(Integer rightGroupId,String name);

    /**
     * 获取管理员
     * */
    JSONObject getManageUser();
}
