package com.enterprise.mapper.pet;

import com.enterprise.base.entity.PetEntity;

import java.util.List;

/**
 * Created by anson on 2018-03-23 10:28:24
 */
public interface PetMapper {

    Integer createOrUpdatePet(PetEntity petEntity);

    Integer updatePet(PetEntity petEntity);

    List<PetEntity> getPetList();

}