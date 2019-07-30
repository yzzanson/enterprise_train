package com.enterprise.mobile.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.BaseController;
import com.enterprise.base.common.MobileLoginUser;
import com.enterprise.base.common.ResultJson;
import com.enterprise.service.question.UserXLibraryService;
import com.enterprise.service.question.UserXQuestionsService;
import com.enterprise.service.user.UserService;
import com.enterprise.service.user.UserXDeptService;
import com.enterprise.util.AssertUtil;
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
@RequestMapping("/user")
public class UserController extends BaseController {

    @Resource
    private UserService userService;

    @Resource
    private UserXLibraryService userXLibraryService;

    @Resource
    private UserXDeptService userXDeptService;

    @Resource
    private UserXQuestionsService userXQuestionsService;

    /**
     * 判断是否是新用户
     * */
    @RequestMapping(value="/isNewbie.json",method= RequestMethod.GET)
    @ResponseBody
    public JSONObject isNewbie(){
        return userService.isNewbie();
    }

    /**
     * 查看用户题库学习进度
     * type
     * 1   热门
     * 2   企业
     * 3   成就
     * 4   钉钉题库
     * */
    @RequestMapping(value="/getUserStudyProcess.json",method= RequestMethod.GET)
    @ResponseBody
    public JSONObject getUserStudyProcess(Integer type){
        try {
            AssertUtil.notNull(type, "类型不能为空!");
            MobileLoginUser mobileLoginUser = MobileLoginUser.getUser();
            return userXLibraryService.getUserStudyProcess(mobileLoginUser.getUserID(), mobileLoginUser.getCompanyID(),type);
        }catch (Exception e){
            return ResultJson.errorResultJson(e.getMessage());
        }
    }

    @RequestMapping(value="/getUserTotalStudy.json",method= RequestMethod.GET)
    @ResponseBody
    public JSONObject getUserTotalStudy(){
        try {
            return userXLibraryService.getUserTotalStudy();
        }catch (Exception e){
            return ResultJson.errorResultJson(e.getMessage());
        }
    }

    /**
     * 查看用户题库学习进度
     * */
    @RequestMapping(value="/getDingLibraryProgress.json",method= RequestMethod.GET)
    @ResponseBody
    public JSONObject getDingLibraryProgress(){
        MobileLoginUser mobileLoginUser = MobileLoginUser.getUser();
        return userXLibraryService.getDingLibraryProgress(mobileLoginUser.getCompanyID(),mobileLoginUser.getUserID());
    }


    /**
     * 查看用户信息
     * */
    @RequestMapping(value="/getUserInfo.json",method= RequestMethod.GET)
    @ResponseBody
    public JSONObject getUserInfo(){
        MobileLoginUser mobileLoginUser = MobileLoginUser.getUser();
        return userService.getUserInfo(mobileLoginUser.getUserID(), mobileLoginUser.getCorpID());
    }

    /**
     * 获取我的部门列表
     * */
    @RequestMapping(value="/getMyDeptList.json",method= RequestMethod.GET)
    @ResponseBody
    public JSONObject getMyDeptList(){
        MobileLoginUser mobileLoginUser = MobileLoginUser.getUser();
        return userXDeptService.getMyDeptList(mobileLoginUser.getCorpID(), mobileLoginUser.getUserID());
    }

    /**
     * 邀请
     * */
    @ResponseBody
    @RequestMapping(value="/invite.json",method= RequestMethod.POST)
    public JSONObject invite(Integer userId){
        return userService.invite(userId);
    }

    /**
     * 获取一个小时之内的用户答题数
     * */
    @RequestMapping(value = "/getHourUserAnswerList.json", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getHourUserAnswergetHourUserAnswerListList(){
        return userXQuestionsService.getHourUserAnswerList();
    }


}
