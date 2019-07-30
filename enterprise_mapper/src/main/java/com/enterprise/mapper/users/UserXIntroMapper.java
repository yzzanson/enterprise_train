package com.enterprise.mapper.users;

import com.enterprise.base.entity.UserXIntrodEntity;
import org.apache.ibatis.annotations.Param;

/**
 * Created by null on 2018-03-23 11:23:39
 */
public interface UserXIntroMapper {

    Integer createUserXIntro(UserXIntrodEntity userIntrodEntity);

    Integer updateUserXIntro(UserXIntrodEntity userIntrodEntity);

    UserXIntrodEntity getByUserId(@Param("userId") Integer userId);
}