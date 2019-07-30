package com.enterprise.service.user;

import com.enterprise.base.entity.UserConfigEntity;
import com.enterprise.base.vo.UserConfigVO;

/**
 * @Description UserService
 * @Author shisan
 * @Date 2018/3/23 上午11:14
 */
public interface UserConfigService {

    Integer createOrUpdateUserConfig(UserConfigEntity userConfigEntity);

    UserConfigVO getByUserId();
}
