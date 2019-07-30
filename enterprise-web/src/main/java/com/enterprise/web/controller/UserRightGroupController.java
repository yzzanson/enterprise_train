package com.enterprise.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.BaseController;
import com.enterprise.service.right.UserRightGroupService;
import com.enterprise.util.AssertUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/7/3 下午2:58
 */
@Controller
@RequestMapping("/user_right_group")
public class UserRightGroupController extends BaseController{

    @Resource
    private UserRightGroupService userRightGroupService;

    /**
     * 保存用户权限组
     * */
    @RequestMapping(value="/saveUserRightGroup.json",method= RequestMethod.POST)
    @ResponseBody
    public JSONObject saveUserRightGroup(String userIds,Integer groupId) {
        return userRightGroupService.saveUserRightGroup(userIds,groupId);
    }

    /**
     * 获取我的菜单列表
     * */
    @RequestMapping(value="/getUserResource.json",method= RequestMethod.GET)
    @ResponseBody
    public JSONObject getUserResource() {
//        return userRightGroupService.getUserResource();
        return userRightGroupService.getUserResource();
    }



    /**
     * 获取我的菜单列表
     * */
    @RequestMapping(value="/getUserResource2.json",method= RequestMethod.GET)
    @ResponseBody
    public JSONObject getUserResource2(String corpId,Integer companyId,Integer userId) {
//        return userRightGroupService.getUserResource();
        return userRightGroupService.getUserResource2(corpId, companyId, userId);
    }



    public static void main(String[] args) {
        String str= "sssssssssss";
        AssertUtil.isTrue(str.length()<10,"角色名长度过长");
    }

}
