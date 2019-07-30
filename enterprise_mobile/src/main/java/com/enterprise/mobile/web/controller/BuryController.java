package com.enterprise.mobile.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.BaseController;
import com.enterprise.base.common.MobileLoginUser;
import com.enterprise.base.common.ResultJson;
import com.enterprise.base.entity.bury.OAOpenBuryEntity;
import com.enterprise.service.bury.BuryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/10/30 下午3:03
 */
@Controller
@RequestMapping("/bury")
public class BuryController extends BaseController{

    @Resource
    private BuryService buryService;


    /**
     * 保存当前的用户进入行为
     */
    @RequestMapping(value = "/saveOpenOA.json", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject saveOpenOA(Integer type) {
        MobileLoginUser mobileLoginUser = MobileLoginUser.getUser();
        OAOpenBuryEntity oaOpenBuryEntity = new OAOpenBuryEntity(mobileLoginUser.getCompanyID(),mobileLoginUser.getUserID(),type,new Date());
        Integer result = buryService.buryOAOpen(oaOpenBuryEntity);
        return ResultJson.succResultJson(result);
    }


}
