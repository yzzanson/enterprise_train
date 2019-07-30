package com.enterprise.service.bagTool.impl;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.Page;
import com.enterprise.base.common.PageEntity;
import com.enterprise.base.common.ResultJson;
import com.enterprise.base.vo.bagtool.BagToolEffectTotalDetailVO;
import com.enterprise.base.vo.bagtool.BagToolEffectTotalVO;
import com.enterprise.mapper.bagTool.BagToolEffectMapper;
import com.enterprise.service.bagTool.BagToolStaticsService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/9/20 下午4:46
 */
@Service
public class BagToolStaticsServiceImpl implements BagToolStaticsService {

    @Resource
    private BagToolEffectMapper bagToolEffectMapper;

    /**
     * 统计架构下正/负效果排行 0/1 负面/正面
     */
    @Override
    public JSONObject getEffectUserSummary(Integer companyId, String month, Integer type, PageEntity pageEntity) {
        //return bagToolStaticsService.getEffectUserSummary(type,pageEntity);
        PageHelper.startPage(pageEntity.getPageNo(), pageEntity.getPageSize());
        if (type == 0) {
            //PageInfo<UserAnswerRankVO> pageInfo = new PageInfo<>(userXQuestionsMapper.getWeekAnswerRankList2(companyId, corpId, startTime, endTime, petUserList, userOpenUserList, remainUserList));
            PageInfo<BagToolEffectTotalVO> pageInfo = new PageInfo<>(bagToolEffectMapper.getNegativeEffectList(companyId, month));
            List<BagToolEffectTotalVO> bagToolEffectList = pageInfo.getList();
            for (int i = 0; i < bagToolEffectList.size(); i++) {
                BagToolEffectTotalVO bagToolEffectTotalVO = bagToolEffectList.get(i);
                //个人每个道具被作用次数
                List<BagToolEffectTotalDetailVO> begToolEffectDetailList = bagToolEffectMapper.getBagToolEffectDetail(companyId, bagToolEffectTotalVO.getUserId(), month,type);
                bagToolEffectTotalVO.setEffectList(begToolEffectDetailList);
            }
            Map<String, Object> dataMap = Maps.newHashMap();
            dataMap.put("list", pageInfo.getList());
            dataMap.put("total", pageInfo.getTotal());
            dataMap.put("page", new Page(pageInfo.getPrePage(), pageEntity.getPageNo(), pageInfo.getNextPage(), pageInfo.getLastPage()));
            return ResultJson.succResultJson(dataMap);
        } else {
            PageInfo<BagToolEffectTotalVO> pageInfo = new PageInfo<>(bagToolEffectMapper.getPositiveEffectList(companyId, month));
            List<BagToolEffectTotalVO> bagToolEffectList = pageInfo.getList();
            for (int i = 0; i < bagToolEffectList.size(); i++) {
                BagToolEffectTotalVO bagToolEffectTotalVO = bagToolEffectList.get(i);
                //个人每个道具被作用次数
                List<BagToolEffectTotalDetailVO> begToolEffectDetailList = bagToolEffectMapper.getBagToolEffectDetail(companyId, bagToolEffectTotalVO.getUserId(), month,type);
                bagToolEffectTotalVO.setEffectList(begToolEffectDetailList);
            }
            Map<String, Object> dataMap = Maps.newHashMap();
            dataMap.put("list", pageInfo.getList());
            dataMap.put("total", pageInfo.getTotal());
            dataMap.put("page", new Page(pageInfo.getPrePage(), pageEntity.getPageNo(), pageInfo.getNextPage(), pageInfo.getLastPage()));
            return ResultJson.succResultJson(dataMap);
        }
    }
}
