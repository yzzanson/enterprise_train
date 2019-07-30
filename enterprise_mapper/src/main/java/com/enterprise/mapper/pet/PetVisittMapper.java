package com.enterprise.mapper.pet;

import com.enterprise.base.entity.PetVisitEntity;
import org.apache.ibatis.annotations.Param;

/**
 * Created by null on 2018-03-23 10:27:03
 */
public interface PetVisittMapper {

    Integer createPetVisit(PetVisitEntity petVisitEntity);

    PetVisitEntity getById(@Param("id") Integer id);
}