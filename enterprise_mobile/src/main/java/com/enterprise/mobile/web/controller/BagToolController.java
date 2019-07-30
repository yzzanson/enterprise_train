package com.enterprise.mobile.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.BaseController;
import com.enterprise.base.common.MobileLoginUser;
import com.enterprise.base.common.PageEntity;
import com.enterprise.base.common.ResultJson;
import com.enterprise.base.enums.StatusEnum;
import com.enterprise.base.vo.bagtool.BagDropVO;
import com.enterprise.base.vo.bagtool.BagToolPeopleVO;
import com.enterprise.mobile.web.interceptor.anno.TokenRequired;
import com.enterprise.service.bagTool.BagToolService;
import com.enterprise.sign.Sign;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/9/5 下午5:53
 */
@Controller
@RequestMapping("/bag_tool")
public class BagToolController extends BaseController {

    @Resource
    private BagToolService bagToolService;

    /**
     * 获取同事说玩法疑问
     */
    @RequestMapping(value = "/getDescript.json", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getDescript() {
        return bagToolService.getDescript();
    }


    /**
     * 获取用户的道具
     */
//    @TokenRequired
    @Sign
    @RequestMapping(value = "/getUserTool.json", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getUserTool() {
        try {
            MobileLoginUser mobileLoginUser = MobileLoginUser.getUser();
            List<BagToolPeopleVO> bagToolPeopleList = bagToolService.getUserToolList(mobileLoginUser.getCompanyID(), mobileLoginUser.getUserID());
            return ResultJson.succResultJson(bagToolPeopleList);
        } catch (Exception e) {
            return ResultJson.errorResultJson(e.getMessage());
        }
    }


    /**
     * 宝箱掉落
     */
    @TokenRequired
    @RequestMapping(value = "/dropBox.json", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject dropBox(Integer type) {
        try {
            BagDropVO bagDropVO = bagToolService.dropBox(type);
            if (bagDropVO.getStatus() != StatusEnum.OK.getValue()) {
                return ResultJson.kickoutResultJson(bagDropVO);
            }
            return ResultJson.succResultJson(bagDropVO);
        } catch (Exception e) {
            return ResultJson.errorResultJson(e.getMessage());
        }
    }


    /**
     * 宝箱打开
     */
    @RequestMapping(value = "/openBox.json", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject openBox(Integer type) {
        try {
            return bagToolService.openBox(type);
        } catch (Exception e) {
            return ResultJson.errorResultJson(e.getMessage());
        }
    }


    /**
     * 使用道具
     *
     * @Param userId使用道具对象
     * @Param toolPeopelId用户道具id
     */
    @RequestMapping(value = "/useTool.json", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject useTool(String dingUserId, Integer toolPeopleId) {
        try {
            return bagToolService.useTool(dingUserId, toolPeopleId);
        } catch (Exception e) {
            return ResultJson.errorResultJson(e.getMessage());
        }
    }

    /**
     * 使用道具
     *
     * @Param userId使用道具对象
     * @Param toolPeopelId用户道具id
     */
    @RequestMapping(value = "/elimateTool.json", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject elimateTool(Integer toolId, Integer count) {
        try {
            return bagToolService.elimateTool(toolId, count);
        } catch (Exception e) {
            return ResultJson.errorResultJson(e.getMessage());
        }
    }


    /**
     * 使用道具
     *
     * @Param effectId 道具作用表bagToolEffect主键
     */
    @RequestMapping(value = "/elimateSingleTool.json", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject elimateSingleTool(Integer effectId) {
        return bagToolService.elimateSingleTool(effectId);
    }


    /**
     * 使用道具
     *
     * @Param userId使用道具对象
     * @Param type 0/1 自己/别人
     */
    @RequestMapping(value = "/getUserPet.json", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getUserPet(Integer userId, Integer type) {
        try {
            if (type == 0) {
                userId = MobileLoginUser.getUser().getUserID();
            }
            return bagToolService.getUserPet(userId, type);
        } catch (Exception e) {
            return ResultJson.errorResultJson(e.getMessage());
        }
    }


    /**
     * 获取我被作用的道具列表
     */
    @RequestMapping(value = "/getMyEffectedTool.json", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getMyEffectedTool(PageEntity pageEntity) {
        try {
            return bagToolService.getMyEffectedTool(pageEntity);
        } catch (Exception e) {
            return ResultJson.errorResultJson(e.getMessage());
        }
    }


    /**
     * 使用道具 掉落次数概率测试
     */
    @RequestMapping(value = "/bagDropTest.json", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject bagDropTest(Integer eventType, Integer count) {
        return bagToolService.bagDropTest(eventType, count);
    }


}
