package com.enterprise.service.report.impl;

import com.enterprise.base.entity.UserXOpenEntity;
import com.enterprise.mapper.users.UserXOpenMapper;
import com.enterprise.service.report.UserXOpenService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/4/13 上午9:57
 */
@Service
public class UserXOpenServiceImpl implements UserXOpenService{

    @Resource
    private UserXOpenMapper userXOpenMapper;

    @Override
    public Integer createOrUpdateUserXOpen(UserXOpenEntity userXOpenEntity) {
        return userXOpenMapper.createUserXOpen(userXOpenEntity);
    }

    @Override
    public Integer getCountByCompanyId(Integer companyId, String date) {
        return userXOpenMapper.getCountByCompanyid(companyId,date);
    }


}
