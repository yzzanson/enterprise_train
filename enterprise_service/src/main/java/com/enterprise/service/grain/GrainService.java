package com.enterprise.service.grain;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.PageEntity;

/**
 * @Description
 * @Author zezhouyang
 * @Date 19/2/12 上午11:32
 */
public interface GrainService {

    JSONObject getGrainActivityList(PageEntity pageEntity);

    //爱心粮活动详情
    JSONObject getGrainActivityDetail(Integer activityId);

    //证书详情
    JSONObject getCertificateDetail(Integer activityId);

    //证书详情-不用登陆
    JSONObject getCertificateDetailNoLogin(Integer id);

    //证书详情
    JSONObject getCertificateList();

    JSONObject grainDonate(Integer activityId);

    //获取用户总数
    JSONObject getUserTotalData();

    JSONObject grainDonateRelease(Integer activityId);
}
