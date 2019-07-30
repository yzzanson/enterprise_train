package com.enterprise.mobile.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.BaseController;
import com.enterprise.service.rank.RankPraiseService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/8/15 上午10:19
 */
@Controller
@RequestMapping("/rank_praise")
public class RankPraiseController extends BaseController{

    @Resource
    private RankPraiseService rankPraiseService;

    /**
     * 我的排名
     * */
    @ResponseBody
    @RequestMapping(value="/praise.json",method= RequestMethod.POST)
    public JSONObject praise(Integer userId){
        return rankPraiseService.praise(userId);
    }

}
