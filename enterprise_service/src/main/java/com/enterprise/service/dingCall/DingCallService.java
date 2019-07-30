package com.enterprise.service.dingCall;

import com.alibaba.fastjson.JSONObject;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/4/18 下午4:42
 */
public interface DingCallService {

    /**
     * 设置发起人
     * */
    JSONObject setCallUserList(String userIds);

    /**
     * 设置接入人
     * */
    JSONObject setAuthUser();

    /**
     * 设置单个接入人
     * */
    JSONObject setAuthUserSingle(Integer companyId);


    /**
     * 设置可拨打电话
     * */
    JSONObject setIsCall();


    /**
     * 设置可拨打电话
     * */
    JSONObject setIsCallSingle(Integer companyId);


    /**
     * 打电话
     * */
    JSONObject raiseCall(String corpId, String raiseUserId);
}
