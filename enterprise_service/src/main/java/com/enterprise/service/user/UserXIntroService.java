package com.enterprise.service.user;

import com.enterprise.base.entity.UserXIntrodEntity;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/4/16 上午10:03
 */
public interface UserXIntroService {

    Integer saveOrUpdateUserXIntro(UserXIntrodEntity userXIntrodEntity);

    UserXIntrodEntity getByUserId(Integer userId);

}
