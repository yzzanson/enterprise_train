package com.enterprise.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.BaseController;
import com.enterprise.base.common.PageEntity;
import com.enterprise.base.common.ResultJson;
import com.enterprise.service.question.UserXQuestionsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @Description
 * @Author zezhouyang
 * @Date 19/3/11 下午2:59
 */
@Controller
@RequestMapping("/rank")
public class RankController extends BaseController {

    @Resource
    private UserXQuestionsService userXQuestionsService;

    /**
     * 学习排行,包括删除的题目
     * */
    @ResponseBody
    @RequestMapping(value="/getStudyRank.json",method= RequestMethod.GET)
    public JSONObject getStudyRank(Integer status,String search,PageEntity pageEntity){
        try {
            return userXQuestionsService.getStudyRank(status, search, pageEntity);
        }catch (Exception e){
            return ResultJson.errorResultJson(e.getMessage());
        }
    }

    /**
     * 学习排行详情-详情
     * type-1/2 企业/官方
     *
     * */
    @ResponseBody
    @RequestMapping(value="/getStudyRankDetail.json",method= RequestMethod.GET)
    public JSONObject getStudyRankDetail(Integer type,Integer userId){
        try {
            return userXQuestionsService.getStudyRankDetail(type, userId);
        } catch (Exception e){
            return ResultJson.errorResultJson(e.getMessage());
        }
    }

    /**
     * 题库热度,企业人答题次数,包括删除的题目
     * type-1/2 企业/官方
     * */
    @ResponseBody
    @RequestMapping(value="/getLibraryHeatRank.json",method= RequestMethod.GET)
    public JSONObject getLibraryHeatRank(Integer type,String search,PageEntity pageEntity){
        try {
            return userXQuestionsService.getLibraryHeatRank(type, search,pageEntity);
        } catch (Exception e){
            return ResultJson.errorResultJson(e.getMessage());
        }
    }


}
