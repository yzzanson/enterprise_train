package com.enterprise.service.petFood;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.MobileLoginUser;

/**
 * @Description PetService
 * @Author anson
 * @Date 2018/4/2 上午9:37
 */
public interface PetFoodService {

    JSONObject initPetFood();

    JSONObject feedPet(MobileLoginUser loginUser);

    JSONObject getPetFood(Integer userId);

    JSONObject feedOtherPet(Integer userId);

    JSONObject feedOtherPetTest(Integer companyId,Integer userId,Integer otherUserId);
}
