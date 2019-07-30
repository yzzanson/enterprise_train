package com.enterprise.service.right;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.entity.right.RightResourceUrlEntity;

import java.util.List;

/**
 * Created by anson on 18/3/24.
 */
public interface RightResourceUrlService {

    List<RightResourceUrlEntity> getResourceByOperate(Integer operator, Integer status,Integer isTop);

    List<RightResourceUrlEntity> findRightResourcesByGroupIds(List<Integer> groupIdList);

    /**
     * 获取权限组下的资源列表
     * */
    JSONObject getResourcesByGroup(Integer groupId);

    /**
     * 保存权限组资源列表
     * */
    JSONObject saveResourceGroup(Integer groupId,String resourceIds);


}