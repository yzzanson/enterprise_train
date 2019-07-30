package com.enterprise.mobile.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.BaseController;
import com.enterprise.service.paperBall.PaperBallService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/10/23 下午4:02
 */
@Controller
@RequestMapping("/pet_paper_ball")
public class PetPaperBallController extends BaseController{

    @Resource
    private PaperBallService paperBallService;

    @RequestMapping(value="/cleanBall.json",method= RequestMethod.POST)
    @ResponseBody
    public JSONObject initPetFood(Integer ballId){
        return paperBallService.cleanBall(ballId);
    }

    /**
     * 获取当前的纸团列表
     * */
    @RequestMapping(value="/getActiveBallList.json",method= RequestMethod.GET)
    @ResponseBody
    public JSONObject getActiveBallList(Integer userId){
        return paperBallService.getActiveBallList(userId);
    }


    /**
     * 获取当前的纸团列表
     * */
    @RequestMapping(value="/initPaperBallType.json",method= RequestMethod.GET)
    @ResponseBody
    public JSONObject initPaperBallType(){
        return paperBallService.setUncleanBallType();
    }



}
