package com.enterprise.mapper.petFood;

import com.enterprise.base.entity.PetFoodPlateEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by null on 2018-03-23 10:27:03
 */
public interface PetFoodPlateMapper {

    Integer createPetFoodPlate(PetFoodPlateEntity petFoodPlateEntity);

    Integer updatePetFoodPlate(PetFoodPlateEntity petFoodPlateEntity);

    PetFoodPlateEntity getPeteFoodPlateByUser(@Param("companyId") Integer companyId,@Param("userId") Integer userId);

    List<PetFoodPlateEntity> getAllRetainFoodList();
}