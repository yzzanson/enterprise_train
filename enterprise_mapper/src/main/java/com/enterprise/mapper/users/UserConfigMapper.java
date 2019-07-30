package com.enterprise.mapper.users;

import com.enterprise.base.entity.UserConfigEntity;
import org.apache.ibatis.annotations.Param;

/**
 * Created by anson on 2018-06-28 10:05:39
 */
public interface UserConfigMapper {

    Integer createUserConfig(UserConfigEntity userConfigEntity);

    Integer updateUserConfig(UserConfigEntity userConfigEntity);

    UserConfigEntity findByUserId(@Param("userId") Integer userId);
}