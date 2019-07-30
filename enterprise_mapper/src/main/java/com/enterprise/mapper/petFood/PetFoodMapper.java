package com.enterprise.mapper.petFood;

import com.enterprise.base.entity.PetFoodEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by null on 2018-03-23 10:27:03
 */
public interface PetFoodMapper {

    Integer createPetFood(PetFoodEntity petFoodEntity);

    Integer updatePetFood(PetFoodEntity petFoodEntity);

    Integer batchInsert(@Param("petFoodList") List<PetFoodEntity> petFoodList);

    PetFoodEntity getPetFood(@Param("companyId") Integer companyId,@Param("userId") Integer userId);

    /**
     * 测试
     * */
    List<PetFoodEntity> getPetFoodList();
}