package com.enterprise.mapper.pet;

import com.enterprise.base.entity.PetDynamicEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by null on 2018-03-23 10:27:03
 */
public interface PetDynamicMapper {

    Integer createPetDynamic(PetDynamicEntity petDynamicEntity);

    List<PetDynamicEntity> getPetDynamicList(@Param("companyId") Integer companyId, @Param("userId") Integer userId);

    PetDynamicEntity getPetDynamic(@Param("companyId") Integer companyId, @Param("userId") Integer userId,@Param("activeId") Integer activeId,@Param("dynamicId") Integer dynamicId);

    Integer updateMyDynamic(@Param("companyId") Integer companyId, @Param("userId") Integer userId, @Param("isRead") Integer isRead);

    Integer getUnreadDynamic(@Param("companyId") Integer companyId, @Param("userId") Integer userId);
}