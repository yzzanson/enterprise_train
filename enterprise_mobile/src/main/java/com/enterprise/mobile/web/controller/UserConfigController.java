package com.enterprise.mobile.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.BaseController;
import com.enterprise.base.common.ResultJson;
import com.enterprise.base.entity.UserConfigEntity;
import com.enterprise.base.vo.UserConfigVO;
import com.enterprise.service.user.UserConfigService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 用户
 * Created by anson on 18/4/2.
 */
@Controller
@RequestMapping("/user_config")
public class UserConfigController extends BaseController {

    @Resource
    private UserConfigService userConfigService;

    /**
     * 判断是否是新用户
     * */
    @RequestMapping(value="/saveOrUpdate.json",method= RequestMethod.POST)
    @ResponseBody
    public JSONObject saveOrUpdate(UserConfigEntity userConfigEntity){
        Integer result = userConfigService.createOrUpdateUserConfig(userConfigEntity);
        if(result>0){
            return ResultJson.succResultJson(result);
        }
        return ResultJson.errorResultJson(result);
    }

    /**
     * 查看用户题库学习进度
     * */
    @RequestMapping(value="/getUserConfig.json",method= RequestMethod.GET)
    @ResponseBody
    public JSONObject getUserConfig(){
        try {
            UserConfigVO userConfigVO = userConfigService.getByUserId();
            return ResultJson.succResultJson(userConfigVO);
        }catch (Exception e){
            return ResultJson.errorResultJson(e.getMessage());
        }
    }

    public static void main(String[] args) {
        UserConfigEntity userConfigEntity = new UserConfigEntity(21,0,1,1);
        System.out.println(ResultJson.succResultJson(userConfigEntity));
    }

}
