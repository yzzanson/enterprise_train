package com.enterprise.service.bagTool;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.PageEntity;
import com.enterprise.base.vo.bagtool.BagDropVO;
import com.enterprise.base.vo.bagtool.BagToolPeopleVO;

import java.util.List;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/9/5 下午5:21
 */
public interface BagToolService {

    /**
     * 获取用户的道具详情
     * */
    List<BagToolPeopleVO> getUserToolList(Integer companyId,Integer userId);

    /**
     * 获取同事说玩法疑问
     * */
    JSONObject getDescript();

    /**
     * 掉落宝箱
     * */
    BagDropVO dropBox(Integer type);

    /**
     * 打开宝箱
     * */
    JSONObject openBox(Integer type);

    /**
     * 使用道具
     * */
    JSONObject useTool(String toUserId,Integer toolPeopleId);

    /**
     * 消除道具作用
     * */
    JSONObject elimateTool(Integer toolId,Integer count);

    /**
     * 消除单个道具作用
     * */
    JSONObject elimateSingleTool(Integer effectId);

    /**
     * 获取用户宠物状态
     * */
    JSONObject getUserPet(Integer userId,Integer type);

    /**
     * 打开箱子测试
     * @Param evenType 事件
     * @Param count 触发次数
     * */
    JSONObject bagDropTest(Integer eventType,Integer count);

    /**
     * 获取我被作用的道具列表
     * */
    JSONObject getMyEffectedTool(PageEntity pageEntity);

    /**
     * 获取还需要回答的题目
     * */
    JSONObject getToolAnswerCount(Integer toolEffectId);

}
