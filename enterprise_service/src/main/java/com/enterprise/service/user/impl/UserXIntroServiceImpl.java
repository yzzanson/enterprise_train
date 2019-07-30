package com.enterprise.service.user.impl;

import com.enterprise.base.entity.UserXIntrodEntity;
import com.enterprise.mapper.users.UserXIntroMapper;
import com.enterprise.service.user.UserXIntroService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/6/28 下午6:53
 */
@Service
public class UserXIntroServiceImpl implements UserXIntroService {

    @Resource
    private UserXIntroMapper userXIntroMapper;

    @Override
    public Integer saveOrUpdateUserXIntro(UserXIntrodEntity userXIntrodEntity) {
        UserXIntrodEntity userXIntrodEntityInDB = userXIntroMapper.getByUserId(userXIntrodEntity.getUserId());
        if(userXIntrodEntityInDB==null){
            userXIntrodEntity.setCreateTime(new Date());
            userXIntrodEntity.setUpdateTime(new Date());
            return userXIntroMapper.createUserXIntro(userXIntrodEntity);
        }else{
            if(userXIntrodEntity.getId()==null){
                userXIntrodEntity.setId(userXIntrodEntityInDB.getId());
            }
            BeanUtils.copyProperties(userXIntrodEntity,userXIntrodEntityInDB);
            userXIntrodEntityInDB.setUpdateTime(new Date());
            return userXIntroMapper.updateUserXIntro(userXIntrodEntityInDB);
        }
    }

    @Override
    public UserXIntrodEntity getByUserId(Integer userId) {
        return userXIntroMapper.getByUserId(userId);
    }
}
