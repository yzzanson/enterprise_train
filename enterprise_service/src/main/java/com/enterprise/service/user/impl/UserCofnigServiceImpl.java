package com.enterprise.service.user.impl;

import com.enterprise.base.common.MobileLoginUser;
import com.enterprise.base.entity.UserConfigEntity;
import com.enterprise.base.vo.UserConfigVO;
import com.enterprise.mapper.users.UserConfigMapper;
import com.enterprise.service.user.UserConfigService;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;

/**
 * @Description
 * @Author zezhouyang
 * @Date 19/1/20 下午6:06
 */
@Service
public class UserCofnigServiceImpl implements UserConfigService {

    @Resource
    private UserConfigMapper userConfigMapper;


    @Override
    public Integer createOrUpdateUserConfig(UserConfigEntity userConfigEntity) {
        MobileLoginUser mobileLoginUser = MobileLoginUser.getUser();
        Integer userId = mobileLoginUser.getUserID();
        userConfigEntity.setUserId(userId);
        if (userConfigMapper.findByUserId(userId) != null) {
            return userConfigMapper.updateUserConfig(userConfigEntity);
        } else {
            return userConfigMapper.createUserConfig(userConfigEntity);
        }
    }

    @Override
    public UserConfigVO getByUserId() {
        UserConfigVO userConfigVO = new UserConfigVO();
        try {
            MobileLoginUser mobileLoginUser = MobileLoginUser.getUser();
            Integer userId = mobileLoginUser.getUserID();
            UserConfigEntity userConfigEntity = userConfigMapper.findByUserId(userId);
            if (userConfigEntity == null) {
                userConfigVO = new UserConfigVO(null, userId, 1, 1, 1, 1);
            } else {
                BeanUtils.copyProperties(userConfigVO, userConfigEntity);
                userConfigVO.setIsNew(0);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return userConfigVO;
    }
}
