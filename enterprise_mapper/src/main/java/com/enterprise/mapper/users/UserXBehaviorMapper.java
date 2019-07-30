package com.enterprise.mapper.users;

import com.enterprise.base.entity.UserXBehavior;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/4/12 下午8:13
 */
public interface UserXBehaviorMapper {

    Integer createUserXBehavior(UserXBehavior userXBehavior);

    Integer updateUserXBeahvior(UserXBehavior userXBehavior);

    UserXBehavior getUserXBehavior(@Param("companyId") Integer companyId, @Param("userId") Integer userId, @Param("type") Integer type, @Param("date") Date date);
}
