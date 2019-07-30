package com.enterprise.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.LoginUser;
import com.enterprise.service.dingCall.DingCallService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @Description
 * @Author zezhouyang
 * @Date 19/1/8 下午2:20
 */
@Controller
@RequestMapping("/dingcall")
public class DingCallController {

    @Resource
    private DingCallService dingCallService;

    @RequestMapping(value = "/setCallUserList.json", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject setCallUserList(String userIds){
        return dingCallService.setCallUserList(userIds);
    }

    /**
     * 设置是否可拨打电话
     * */
    @RequestMapping(value = "/setIsCall.json", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject setIsCall(){
        return dingCallService.setIsCall();
    }

    /**
     * 设置是否可拨打电话
     * */
    @RequestMapping(value = "/setIsCallSingle.json", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject setIsCallSingle(Integer companyId){
        return dingCallService.setIsCallSingle(companyId);
    }



    /**
     * 设置接入人
     * */
    @RequestMapping(value = "/setAuthUser.json", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject setAuthUser(){
        return dingCallService.setAuthUser();
    }

    /**
     * 设置接入人
     * */
    @RequestMapping(value = "/setAuthUserSingle.json", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject setAuthUserSingle(Integer companyId){
        return dingCallService.setAuthUserSingle(companyId);
    }

    /**
     * 打电话
     * */
    @RequestMapping(value = "/raiseCall.json", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject raiseCall(String corpId){
        LoginUser loginUser = LoginUser.getUser();
        String dingUserId = loginUser.getDingID();
        if(loginUser.getUserID().equals(4) || loginUser.getUserID().equals(96) || loginUser.getUserID().equals(936)){
            dingUserId = "032261275826281866";
        }else if(loginUser.getUserID().equals(79)){
            dingUserId = "0731543202-1606789160";
        }else if(loginUser.getUserID().equals(21)){
            dingUserId = "045122333326332477";
        }
        return dingCallService.raiseCall(corpId,dingUserId);
    }
}
