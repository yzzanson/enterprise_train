package com.enterprise.mapper.grain;

import com.enterprise.base.entity.UserGrainActivityEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/10/23 上午10:31
 */
public interface UserGrainActivityMapper {

    Integer createUserGrainActivity(UserGrainActivityEntity userGrainActivityEntity);

    //用户活动捐助情况
    UserGrainActivityEntity getByCompanyAndUser(@Param("companyId") Integer companyId,@Param("activityId") Integer activityId,@Param("userId") Integer userId);

    //用户活动捐助情况id
    UserGrainActivityEntity getById(@Param("id") Integer id);

    //当前活动捐助数
    Integer getSponsorCountByActivityId(@Param("activityId") Integer activityId);

    //用户证书列表
    List<UserGrainActivityEntity> getListByUser(@Param("companyId") Integer companyId,@Param("userId") Integer userId);
}
