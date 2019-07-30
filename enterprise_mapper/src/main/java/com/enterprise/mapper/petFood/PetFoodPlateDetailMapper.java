package com.enterprise.mapper.petFood;

import com.enterprise.base.entity.PetFoodPlateDetailEntity;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by null on 2018-03-23 10:27:03
 */
public interface PetFoodPlateDetailMapper {

    Integer createPetFoodPlateDetail(PetFoodPlateDetailEntity petFoodPlateDetailEntity);

    Integer updatePetFoodDetail(PetFoodPlateDetailEntity petFoodPlateDetailEntity);

    PetFoodPlateDetailEntity getLatestFeedRecord(@Param("companyId") Integer companyId, @Param("userId") Integer userId, @Param("type") Integer type, @Param("status") Integer status,@Param("time") Date time);

    PetFoodPlateDetailEntity getPlateConsumeRecord(@Param("companyId") Integer companyId,@Param("userId") Integer userId,@Param("plateDetailId") Integer plateDetailId,@Param("status") Integer status);

    /**
     * 喂养其他人的次数
     * */
    Integer getFeedOtherCount(@Param("companyId") Integer companyId, @Param("userId") Integer userId, @Param("feedUserId") Integer feedUserId, @Param("type") Integer type,@Param("time") String time);

    /**
     * 上次消耗时间
     * */
    PetFoodPlateDetailEntity getLatestConsumeRecord(@Param("companyId") Integer companyId, @Param("userId") Integer userId, @Param("type") Integer type);

    PetFoodPlateDetailEntity getById(@Param("id") Integer id);

    List<PetFoodPlateDetailEntity> getOtherFeedList(@Param("companyId") Integer companyId,@Param("userId") Integer userId);

    List<PetFoodPlateDetailEntity> getFeedList(@Param("companyId") Integer companyId,@Param("userId") Integer userId);
}