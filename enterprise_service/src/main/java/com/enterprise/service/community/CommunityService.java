package com.enterprise.service.community;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.PageEntity;

/**
 * @Description
 * @Author zezhouyang
 * @Date 19/03/25 上午11:17
 */
public interface CommunityService {

    /**
     *
     * @author anson
     * @date 19/03/25 上午11:18
     */
    JSONObject checkStudySchedule();

    /**
     *
     * @author anson
     * @date 19/03/25 下午14:04
     */
    JSONObject inviteManage();

    /**
     *
     * @author anson
     * @date 19/03/25 下午14:43
     */
    JSONObject getManageList(PageEntity pageEntity);

    /**
     *
     * @author anson
     * @date 19/03/25 下午15:32
     */
    JSONObject sendInvite(Integer userId);

    /**
     * 保存报名情况
     * @date 19/03/25 下午16:45
     * */
    JSONObject saveInvite(String name,String phoneNum,Integer companyId,Integer userId);


    /**
     * 保存报名情况
     * @date 19/03/25 下午16:45
     * */
    JSONObject checkIsInvite();

}

