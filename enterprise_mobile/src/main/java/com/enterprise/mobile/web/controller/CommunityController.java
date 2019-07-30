package com.enterprise.mobile.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.BaseController;
import com.enterprise.base.common.MobileLoginUser;
import com.enterprise.base.common.PageEntity;
import com.enterprise.service.community.CommunityService;
import com.enterprise.util.AssertUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Description
 * @Author zezhouyang
 * @Date 19/03/25 上午11:10
 */
@Controller
@RequestMapping("/community")
public class CommunityController extends BaseController{

    @Resource
    private CommunityService communityService;

    /**
     * 验证是否有5名员工完成官方题库
     */
    @RequestMapping("/checkStudySchedule.json")
    @ResponseBody
    public JSONObject checkStudySchedule() throws Exception {
        return communityService.checkStudySchedule();
    }


    /**
     * 召唤管理员开通-0/1 跳转开通页面/管理员列表
     */
    @RequestMapping("/inviteManage.json")
    @ResponseBody
    public JSONObject inviteManage() throws Exception {
        return communityService.inviteManage();
    }

    /**
     * 获取管理员列表
     */
    @RequestMapping("/getManageList.json")
    @ResponseBody
    public JSONObject getManageList(PageEntity pageEntity) throws Exception {
        return communityService.getManageList(pageEntity);
    }


    /**
     * 发送邀请开通
     */
    @RequestMapping(value = "/sendInvite.json", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject sendInvite(Integer userId) {
        return communityService.sendInvite(userId);
    }


    /**
     * 保存开通人员信息
     */
    @RequestMapping(value = "/saveInvite.json", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject saveInvite(HttpServletRequest request) {
        String name = request.getParameter("name");
        String phoneNum = request.getParameter("phoneNum");
        AssertUtil.isTrue(name.length() <= 10, "名字超过最大限制!");
        AssertUtil.isTrue(phoneNum.length() <= 11, "手机号超过限制!");
        MobileLoginUser mobileLoginUser = MobileLoginUser.getUser();
        Integer companyId = null;
        Integer userId = null;
        if(mobileLoginUser==null){
            companyId = Integer.valueOf(request.getParameter("companyId"));
            userId = Integer.valueOf(request.getParameter("userId"));
        }else{
            companyId = mobileLoginUser.getCompanyID();
            userId = mobileLoginUser.getUserID();
        }
        return communityService.saveInvite(name, phoneNum , companyId,userId);
    }


    /**
     * 检查是否报名开通企业社区
     */
    @RequestMapping(value = "/checkIsInvite.json")
    @ResponseBody
    public JSONObject checkIsInvite() {
        return communityService.checkIsInvite();
    }


    public static void main(String[] args) {
        String name = "小样";
        AssertUtil.isTrue(name.length() <= 10, "名字超过最大限制!");
    }
}
