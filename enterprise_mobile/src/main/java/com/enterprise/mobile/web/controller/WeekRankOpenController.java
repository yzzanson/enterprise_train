package com.enterprise.mobile.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.BaseController;
import com.enterprise.base.common.MobileLoginUser;
import com.enterprise.base.common.ResultJson;
import com.enterprise.service.rank.WeekRankOpenService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/9/17 上午11:47
 */
@Controller
@RequestMapping("/week_rank_open")
public class WeekRankOpenController extends BaseController{

    @Resource
    private WeekRankOpenService weekRankOpenService;

    /**
     * 是否打开过
     * */
    @RequestMapping(value="/checkIsOpen.json",method= RequestMethod.GET)
    @ResponseBody
    public JSONObject checkIsOpen() {
        Integer openResult =  weekRankOpenService.checkIsOpen();
        if(openResult>0){
            return ResultJson.succResultJson("已经打开过");
        }
        return ResultJson.errorResultJson("");
    }

    /**
     * 打开周排行
     * */
    @RequestMapping(value="/openWeekRank.json",method= RequestMethod.POST)
    @ResponseBody
    public JSONObject openWeekRank() {
        MobileLoginUser mobileLoginUser = MobileLoginUser.getUser();
        Integer openResult =  weekRankOpenService.openWeekRank(mobileLoginUser.getCompanyID(),mobileLoginUser.getUserID());
        if(openResult>0){
            return ResultJson.succResultJson("保存成功");
        }
        return ResultJson.errorResultJson("保存失败");
    }

}
