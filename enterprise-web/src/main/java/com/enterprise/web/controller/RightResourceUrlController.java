package com.enterprise.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.BaseController;
import com.enterprise.service.right.RightResourceUrlService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by anson on 18/3/29.
 */
@Controller
@RequestMapping("/right_resource_url")
public class RightResourceUrlController extends BaseController {

    @Resource
    private RightResourceUrlService rightResourceUrlService;

    /**
     * 获取当前权限组的所有资源
     */
    @RequestMapping(value = "/getResourcesByGroup.json", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getResourcesByGroup(Integer groupId) {
        return rightResourceUrlService.getResourcesByGroup(groupId);
    }


    /**
     * 保存权限组的资源
     */
    @RequestMapping(value = "/saveResourceGroup.json", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject saveResourceGrouo(Integer groupId,String resourceIds) {
        return rightResourceUrlService.saveResourceGroup(groupId,resourceIds);
    }







}
