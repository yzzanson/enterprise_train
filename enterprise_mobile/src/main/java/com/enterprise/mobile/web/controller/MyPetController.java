package com.enterprise.mobile.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.BaseController;
import com.enterprise.base.common.MobileLoginUser;
import com.enterprise.base.common.ResultJson;
import com.enterprise.base.entity.MyPetEntity;
import com.enterprise.service.pet.MyPetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by anson on 18/4/2.
 */
@Controller
@RequestMapping("/mypet")
public class MyPetController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private MyPetService myPetService;


    /**
     * 我的宠物-新增or修改
     * */
    @RequestMapping(value="/createOrUpdate.json",method= RequestMethod.POST)
    @ResponseBody
    public JSONObject createOrUpdateMyPet(MyPetEntity myPetEntity){
        MobileLoginUser mobileLoginUser = MobileLoginUser.getUser();
        myPetEntity.setUserId(mobileLoginUser.getUserID());
        Integer result = myPetService.createOrUpdatePet(myPetEntity);
        if (result>0)
            return ResultJson.succResultJson(myPetEntity);
        else
            return ResultJson.errorResultJson("pet_saveOrUpdate失败");
    }

    /**
     * 修改名字
     * */
    @RequestMapping(value="/update.json",method= RequestMethod.POST)
    @ResponseBody
    public JSONObject update(String name){
        Integer result = myPetService.update(name);
        if (result>0)
            return ResultJson.succResultJson("修改成功");
        else
            return ResultJson.errorResultJson("修改名字失败");
    }


    /**
     * 我的宠物-获取我的宠物
     * */
    @RequestMapping("/getMyPet.json")
    @ResponseBody
    public JSONObject getMyPet() {
        try {
            MobileLoginUser mobileLoginUser = MobileLoginUser.getUser();
            JSONObject questionFeedBackJson = myPetService.getMyPet(mobileLoginUser.getUserID());
            return questionFeedBackJson;
        } catch (Exception e) {
            logger.error("pet_getPetList异常:"+e.getMessage());
        }
        return ResultJson.succResultJson("SUCCESS", null);
    }

    /**
     * 获取宠物的碎碎念--》部门间>个人>宠物随机的
     * */
    @RequestMapping("/getPetWords.json")
    @ResponseBody
    public JSONObject getPetWords(Integer userId) {
        return myPetService.getPetWords(userId);
    }


    /**
     * 初始化用户体重
     * */
    @RequestMapping("/initWeight.json")
    @ResponseBody
    public JSONObject initWeight() {
        return myPetService.initWeight();
    }





    public static void main(String[] args) {
//        String str = "哼 随便你了";
//        String str = "近朱者赤，近我者甜⁄(⁄ ⁄•⁄ω⁄•⁄ ⁄)⁄";
//        str.replaceAll("\\u00A0","");
        System.out.println(ResultJson.succResultJson("修改成功"));
    }

}
