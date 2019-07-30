package com.enterprise.controller;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.BaseController;
import com.enterprise.base.common.ResultJson;
import com.enterprise.mapper.users.UserMapper;
import com.enterprise.service.question.UserXLibraryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/10/12 上午10:47
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController{

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserXLibraryService userXLibraryService;

    /**
     * 同步所有管理
     */
    @RequestMapping("/initUser.json")
    @ResponseBody
    public JSONObject initUser(Integer userId) {
        Integer result = userMapper.callInitUserDataFunction(userId);
        return ResultJson.succResultJson(result);
    }

    /**
     * 同步所有管理
     */
    @RequestMapping("/updateFinishTime.json")
    @ResponseBody
    public JSONObject updateFinishTime() {
        return userXLibraryService.updateFinishTime();
    }

}
