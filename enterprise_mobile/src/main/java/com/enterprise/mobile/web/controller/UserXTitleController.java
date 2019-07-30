package com.enterprise.mobile.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.BaseController;
import com.enterprise.base.common.MobileLoginUser;
import com.enterprise.base.common.ResultJson;
import com.enterprise.base.vo.UserXTitleVO;
import com.enterprise.service.user.UserXTitleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/6/28 下午2:19
 */
@Controller
@RequestMapping("/user_x_title")
public class UserXTitleController extends BaseController{

    @Resource
    private UserXTitleService userXTitleService;

    /**
     * 可佩带的头衔列表
     * */
    @RequestMapping(value="/getTitleList.json",method= RequestMethod.GET)
    @ResponseBody
    public JSONObject getTitleList() {
        MobileLoginUser mobileLoginUser = MobileLoginUser.getUser();
        List<UserXTitleVO> userXTitleVOList = userXTitleService.findUserXTitleListByCompany(mobileLoginUser.getCompanyID(), mobileLoginUser.getUserID());
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("data",userXTitleVOList);
        resultMap.put("avatar",mobileLoginUser.getAvatar());
        return ResultJson.succResultJson(resultMap);
    }

    /**
     * 佩带头衔
     * */
    @RequestMapping(value="/wearTitle.json",method= RequestMethod.POST)
    @ResponseBody
    public JSONObject wearTitle(Integer id) {
        try {
            MobileLoginUser mobileLoginUser = MobileLoginUser.getUser();
            return userXTitleService.wearTitle(mobileLoginUser.getCompanyID(), mobileLoginUser.getUserID(), id);
        }catch (Exception e){
            return ResultJson.errorResultJson(e.getMessage());
        }
    }


}
