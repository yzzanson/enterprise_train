package com.enterprise.service.pet;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.PageEntity;
import com.enterprise.base.entity.PetEntity;

/**
 * @Description PetService
 * @Author anson
 * @Date 2018/4/2 上午9:37
 */
public interface PetService {

    Integer createOrUpdatePet(PetEntity petEntity);

    JSONObject getPetList(PageEntity pageEntity);

    JSONObject getRandomPetName();
}
