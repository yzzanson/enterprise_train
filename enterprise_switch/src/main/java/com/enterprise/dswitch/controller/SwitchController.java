package com.enterprise.dswitch.controller;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.ResultJson;
import com.enterprise.base.entity.DSwitchEntity;
import com.enterprise.service.dswitch.DSwitchService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/12/14 下午4:12
 */
@Controller
@RequestMapping("/switch")
public class SwitchController {

    @Resource
    private DSwitchService dSwitchService;

    /**
     * 获取用户默认的题目
     */
    @RequestMapping(value = "/swichChange.json", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject swichChange(Integer type,Integer status) {
        DSwitchEntity dSwitchEntity = new DSwitchEntity(type,status,new Date(),new Date());
        Integer result = dSwitchService.createOrUpdateDSwitch(dSwitchEntity);
        return ResultJson.succResultJson(result);
    }

    /**
     * 获取用户默认的题目
     */
    @RequestMapping(value = "/getSwitch.json", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getSwitch(Integer type) {
        DSwitchEntity dSwitchEntity = dSwitchService.getOne(type);
        return ResultJson.succResultJson(dSwitchEntity);
    }

}
