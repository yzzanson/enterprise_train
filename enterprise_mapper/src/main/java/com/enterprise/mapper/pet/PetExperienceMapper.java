package com.enterprise.mapper.pet;

import com.enterprise.base.entity.PetExperienceEntity;
import org.apache.ibatis.annotations.Param;

/**
 * Created by null on 2018-03-23 10:27:03
 */
public interface PetExperienceMapper {

    PetExperienceEntity getByLevel(@Param("level") Integer level);

    PetExperienceEntity getByExperienceLow(@Param("low") Integer low);

    PetExperienceEntity getByExperience(@Param("ecperienceValue") Integer ecperienceValue);

    PetExperienceEntity getMaxLevel();
}