package com.enterprise.mobile.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.BaseController;
import com.enterprise.base.common.PageEntity;
import com.enterprise.service.grain.GrainService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @Description
 * @Author zezhouyang
 * @Date 19/2/12 上午11:22
 */
@Controller
@RequestMapping("/grain")
public class GrainController extends BaseController {

    @Resource
    private GrainService grainService;

    /**
     * 获取爱心粮列表
     */
    @RequestMapping(value = "/getGrainActivityList.json", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getGrainActivityList(PageEntity pageEntity) {
        return grainService.getGrainActivityList(pageEntity);
    }


    /**
     * 获取爱心粮详情
     */
    @RequestMapping(value = "/getGrainActivityDetail.json", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getGrainActivityDetail(Integer activityId) {
        return grainService.getGrainActivityDetail(activityId);
    }


    /**
     * 获取证书详情
     */
    @RequestMapping(value = "/getCertificateDetail.json", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getCertificateDetail(Integer activityId) {
        return grainService.getCertificateDetail(activityId);
    }


    /**
     * 获取证书详情
     */
    @RequestMapping(value = "/getCertificateDetailNoLogin.json", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getCertificateDetailNoLogin(Integer id) {
        return grainService.getCertificateDetailNoLogin(id);
    }


    /**
     * 获取证书列表
     */
    @RequestMapping(value = "/getCertificateList.json", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getCertificateList() {
        return grainService.getCertificateList();
    }


    /**
     * 爱心捐助
     */
    @RequestMapping(value = "/grainDonate.json", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject grainDonate(Integer activityId) {
        return grainService.grainDonate(activityId);
    }



    /**
     * 爱心捐助

    @RequestMapping(value = "/grainDonateRelease.json", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject grainDonateRelease(Integer activityId) {
        return grainService.grainDonateRelease(activityId);
    }
     */

    /**
     * 获取用户总数
     * */
    @RequestMapping(value = "/getUserTotalData.json", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getUserTotalData(){
        return grainService.getUserTotalData();
    }


}
