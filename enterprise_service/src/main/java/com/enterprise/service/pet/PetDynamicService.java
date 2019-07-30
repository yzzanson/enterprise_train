package com.enterprise.service.pet;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.PageEntity;

/**
 * @Description PetService
 * @Author anson
 * @Date 2018/4/2 上午9:37
 */
public interface PetDynamicService {

    JSONObject getPetDynamic(PageEntity pageEntity);

    JSONObject updateMyPetDynamic();

    /**
     * 串门
     * */
    JSONObject vistOther(Integer userId);
}
