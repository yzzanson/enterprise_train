package com.enterprise.service.user.impl;

import com.enterprise.base.entity.UserXBehavior;
import com.enterprise.mapper.users.UserXBehaviorMapper;
import com.enterprise.service.user.UserXBehaviorService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/4/16 上午10:04
 */
@Service
public class UserXBehaviorServiceImpl implements UserXBehaviorService{

    @Resource
    private UserXBehaviorMapper userXBehaviorMapper;

    @Override
    public void createOrUpdateUserXBehavior(UserXBehavior userXBehavior) {
        if(userXBehavior.getId()!=null){
            userXBehaviorMapper.updateUserXBeahvior(userXBehavior);
        }else{
            userXBehaviorMapper.createUserXBehavior(userXBehavior);
        }
    }

}
