package com.enterprise.mapper.petFood;

import com.enterprise.base.entity.PetFoodDetailEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by null on 2018-03-23 10:27:03
 */
public interface PetFoodDetailMapper {

    Integer createPetFoodDetail(PetFoodDetailEntity petFoodDetailEntity);

    Integer updatePetFoodDetail(PetFoodDetailEntity petFoodDetailEntity);

    Integer batchInsert(@Param("petFoodDetailList") List<PetFoodDetailEntity> petFoodDetailList);

    PetFoodDetailEntity getByCompanyAndUserId(@Param("companyId") Integer companyId,@Param("userId") Integer userId);

    Integer getUserPetFoodCount(@Param("companyId") Integer companyId,@Param("userId") Integer userId);

}