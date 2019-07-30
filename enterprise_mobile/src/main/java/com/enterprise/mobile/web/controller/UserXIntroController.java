package com.enterprise.mobile.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.BaseController;
import com.enterprise.base.common.MobileLoginUser;
import com.enterprise.base.common.ResultJson;
import com.enterprise.base.entity.UserXIntrodEntity;
import com.enterprise.mobile.web.interceptor.anno.TokenRequired;
import com.enterprise.service.user.UserXIntroService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/6/28 下午2:19
 */
@Controller
@RequestMapping("/user_x_intro")
public class UserXIntroController extends BaseController {

    @Resource
    private UserXIntroService userXIntroService;

    /**
     * 保存用户记录
     */
    @RequestMapping(value = "/createOrUpdate.json", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject createOrUpdate(UserXIntrodEntity userXIntrodEntity) {
        try {
            MobileLoginUser mobileLoginUser = MobileLoginUser.getUser();
            userXIntrodEntity.setUserId(mobileLoginUser.getUserID());
            Integer result = userXIntroService.saveOrUpdateUserXIntro(userXIntrodEntity);
            return ResultJson.succResultJson(userXIntrodEntity);
        } catch (Exception e) {
            return ResultJson.errorResultJson(e.getMessage());
        }
    }

    /**
     * 获取用户触发事件
     */
    @TokenRequired
    @RequestMapping(value = "/getByUserId.json", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getByUserId() {
        MobileLoginUser mobileLoginUser = MobileLoginUser.getUser();
        UserXIntrodEntity userXIntrodEntity = userXIntroService.getByUserId(mobileLoginUser.getUserID());
        if (userXIntrodEntity == null) {
            //public UserXIntrodEntity(Integer userId, Integer growthAnswer, Integer chooseLib, Integer wrongAnswer, Integer arena, Integer arenaInit, Integer arenaTime, Integer chooseTeam, Integer challenge, Integer bag, Integer newVersion, Integer feedPet, Integer otherFeedPet, Integer cleanPet, Integer answer)
            userXIntrodEntity = new UserXIntrodEntity(null, mobileLoginUser.getUserID(), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        }
        return ResultJson.succResultJson(userXIntrodEntity);
    }


}
