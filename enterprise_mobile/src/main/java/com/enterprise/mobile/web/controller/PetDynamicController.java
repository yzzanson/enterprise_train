package com.enterprise.mobile.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.BaseController;
import com.enterprise.base.common.PageEntity;
import com.enterprise.base.common.ResultJson;
import com.enterprise.service.pet.PetDynamicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/10/26 下午5:38
 */
@Controller
@RequestMapping("/pet_dynamic")
public class PetDynamicController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private PetDynamicService petDynamicService;

    /**
     * 我的动态
     */
    @RequestMapping(value = "/getPetDynamic.json", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getPetActive(PageEntity pageEntity) {
        return petDynamicService.getPetDynamic(pageEntity);
    }

    /**
     * 更新我的动态
     */
    @RequestMapping(value = "/updateMyPetDynamic.json", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject updateMyPetDynamic() {
        return petDynamicService.updateMyPetDynamic();
    }


    /**
     * 串门
     *
     * @Param userId  访问的用户id
     */
    @RequestMapping(value = "/vistOther.json", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject vistOther(Integer userId) {
        try {
            return petDynamicService.vistOther(userId);
        } catch (Exception e) {
            return ResultJson.errorResultJson(e.getMessage());
        }
    }

}
