package com.enterprise.service.user.impl;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.ResultJson;
import com.enterprise.base.entity.UserXDeptEntity;
import com.enterprise.mapper.users.UserXDeptMapper;
import com.enterprise.service.user.UserXDeptService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/4/16 上午10:04
 */
@Service
public class UserXDeptServiceImpl implements UserXDeptService {

    @Resource
    private UserXDeptMapper userXDeptMapper;

    @Override
    public Integer getDepartmentId(String corpId, Integer userId) {
        Integer depptId = userXDeptMapper.getUserDept(corpId, userId);
        return depptId;
    }

    @Override
    public UserXDeptEntity getUserXDeptByCorpIdAndDeptId(UserXDeptEntity userXDeptEntity) {
        return userXDeptMapper.getUserXDeptByCorpIdAndDeptId(userXDeptEntity);
    }

    @Override
    public void createUserXDept(UserXDeptEntity userXDeptEntity) {
        if (userXDeptEntity.getId() != null) {
            userXDeptMapper.updateUserXDept(userXDeptEntity);
        } else {
            userXDeptMapper.createUserXDept(userXDeptEntity);
        }
    }

    @Override
    public List<UserXDeptEntity> getUserDepts(String corpId, Integer userId, Integer type) {
        return userXDeptMapper.getUserDepts(corpId, userId, type);
    }

    @Override
    public void batchSafeDelte(UserXDeptEntity userXDeptEntity) {
        userXDeptMapper.batchSafeDelte(userXDeptEntity);
    }

    @Override
    public UserXDeptEntity getByUserIdAndCorpIdAndDeptId(UserXDeptEntity userXDeptEntity) {
        return userXDeptMapper.getByUserIdAndCorpIdAndDeptId(userXDeptEntity.getUserId(), userXDeptEntity.getCorpId(), userXDeptEntity.getDeptId());
    }

    @Override
    public JSONObject getMyDeptList(String corpId, Integer userId) {
        return ResultJson.succResultJson(userXDeptMapper.getMyDeptList(corpId, userId));
    }


}
