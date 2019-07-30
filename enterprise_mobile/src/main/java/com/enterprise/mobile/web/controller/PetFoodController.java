package com.enterprise.mobile.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.BaseController;
import com.enterprise.base.common.MobileLoginUser;
import com.enterprise.service.petFood.PetFoodService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/10/22 上午11:59
 */
@Controller
@RequestMapping("/pet_food")
public class PetFoodController extends BaseController{

    @Resource
    private PetFoodService petFoodService;

    /**
     * 每个人初始化获取100g猫粮
     * */
    @RequestMapping(value="/initPetFood.json",method= RequestMethod.GET)
    @ResponseBody
    public JSONObject initPetFood(){
        return petFoodService.initPetFood();
    }


    /**
     * 喂食,每次固定120g
     * */
    @RequestMapping(value="/feedPet.json",method= RequestMethod.POST)
    @ResponseBody
    public JSONObject feedPet(){
        MobileLoginUser loginUser = MobileLoginUser.getUser();
        return petFoodService.feedPet(loginUser);
    }

    /**
     * 获取当前剩余猫粮
     * */
    @RequestMapping(value="/getPetFood.json",method= RequestMethod.GET)
    @ResponseBody
    public JSONObject getPetFood(Integer userId){
        return petFoodService.getPetFood(userId);
    }


    /**
     * 给别人喂食,每次固定10g
     * */
    @RequestMapping(value="/feedOtherPet.json",method= RequestMethod.POST)
    @ResponseBody
    public JSONObject feedOtherPet(Integer userId){
        return petFoodService.feedOtherPet(userId);
    }


}
