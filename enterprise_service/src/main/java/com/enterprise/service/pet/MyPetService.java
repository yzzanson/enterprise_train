package com.enterprise.service.pet;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.entity.MyPetEntity;

/**
 * @Description PetService
 * @Author anson
 * @Date 2018/4/2 上午9:37
 */
public interface MyPetService {

    Integer createOrUpdatePet(MyPetEntity myPetEntity);

    Integer update(String name);

    JSONObject getMyPet(Integer userId);

    /**
     * 获取宠物说过的话
     * */
    JSONObject getPetWords(Integer userId);

    JSONObject initWeight();
}
