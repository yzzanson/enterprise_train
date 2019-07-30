package com.enterprise.service.bagTool;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.PageEntity;

/**
 * @Description 道具统计
 * @Author zezhouyang
 * @Date 18/9/20 下午4:46
 */
public interface BagToolStaticsService {

    /**
     * 统计架构下正/负效果排行 0/1 负面/正面
     */
    JSONObject getEffectUserSummary(Integer companyId,String month,Integer type,PageEntity pageEntity);

}
