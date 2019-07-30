package com.enterprise.mobile.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.BaseController;
import com.enterprise.base.common.PageEntity;
import com.enterprise.base.common.ResultJson;
import com.enterprise.base.entity.PetEntity;
import com.enterprise.base.entity.PetExperienceEntity;
import com.enterprise.mapper.pet.PetExperienceMapper;
import com.enterprise.service.pet.PetService;
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
@RequestMapping("/pet")
public class PetController extends BaseController {


    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private PetService petService;

    @Resource
    private PetExperienceMapper petExperienceMapper;

    /**
     * 宠物蛋-新增or修改
     * */
    @RequestMapping(value="/createOrUpdate.json",method= RequestMethod.POST)
    @ResponseBody
    public JSONObject createOrUpdatePet(PetEntity petEntity){
//        MobileLoginUser mobileLoginUser = MobileLoginUser.getUser();
        Integer result = petService.createOrUpdatePet(petEntity);
        if (result>0)
            return ResultJson.succResultJson(petEntity);
        else
            return ResultJson.errorResultJson("pet_saveOrUpdate失败");
    }

    /**
     * 宠物蛋-获取宠物蛋列表
     * */
    @RequestMapping(value="/getPetList.json",method= RequestMethod.GET)
    @ResponseBody
    public JSONObject getPetList(PageEntity pageEntity) {
        try {
            JSONObject questionFeedBackJson = petService.getPetList(pageEntity);
            return questionFeedBackJson;
        } catch (Exception e) {
            logger.error("pet_getPetList异常:"+e.getMessage());
        }
        return ResultJson.succResultJson("SUCCESS", null);
    }

    @RequestMapping("/getPetExperierience.json")
    @ResponseBody
    public JSONObject getPetExperierience(Integer level) {
        PetExperienceEntity petExperienceEntity = petExperienceMapper.getByLevel(level);
        return ResultJson.succResultJson(petExperienceEntity);
    }

    @RequestMapping("/getRandomPetName.json")
    @ResponseBody
    public JSONObject getRandomPetName() {
        return petService.getRandomPetName();
    }





}
