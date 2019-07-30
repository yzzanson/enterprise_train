package com.enterprise.mobile.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.BaseController;
import com.enterprise.base.common.PageEntity;
import com.enterprise.service.bagTool.BagToolStaticsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/9/5 下午5:53
 */
@Controller
@RequestMapping("/bag_tool_statics")
public class BagToolStaticsController extends BaseController {

    @Resource
    private BagToolStaticsService bagToolStaticsService;

    /**
     * 获取同事说玩法疑问
     * @Param type0/1  负面/正面(不包括自己对自己用的,不包括回魂丹)
     */
    @RequestMapping(value = "/getEffectTotalSummary.json", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getEffectTotalSummary(Integer companyId,String month,Integer type,PageEntity pageEntity) {
        return bagToolStaticsService.getEffectUserSummary(companyId,month,type,pageEntity);
    }


}
