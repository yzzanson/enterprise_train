package com.enterprise.service.rank;

import com.alibaba.fastjson.JSONObject;

import java.util.Date;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/8/14 下午3:20
 */
public interface RankPraiseService {

    JSONObject praise(Integer userId);

    Integer deleteExpireData(Date beginTime);

}
