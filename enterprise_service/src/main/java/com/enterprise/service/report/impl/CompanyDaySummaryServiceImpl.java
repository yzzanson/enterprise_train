package com.enterprise.service.report.impl;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.ResultJson;
import com.enterprise.base.entity.CompanyDaySummaryEntity;
import com.enterprise.mapper.questions.UserXQuestionsMapper;
import com.enterprise.mapper.report.CompanyDaySummaryMapper;
import com.enterprise.service.report.CompanyDaySummaryService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/4/13 上午9:57
 */
@Deprecated
@Service
public class CompanyDaySummaryServiceImpl implements CompanyDaySummaryService{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private CompanyDaySummaryMapper companyDaySummaryMapper;

    @Resource
    private UserXQuestionsMapper userXQuestionsMapper;

    @Override
    public Integer createCompanyDaySummary(CompanyDaySummaryEntity companyDaySummaryEntity) {
        return companyDaySummaryMapper.createCompayDaySummary(companyDaySummaryEntity);
    }

    @Override
    public Integer updateCompanySummary(CompanyDaySummaryEntity companyDaySummaryEntity) {
        return companyDaySummaryMapper.updateCompanySummary(companyDaySummaryEntity);
    }

    @Override
    public JSONObject updateCompanyDaySummary(String date) {
        List<CompanyDaySummaryEntity> updateCompanyDayList = new ArrayList<>();
        List<CompanyDaySummaryEntity> companyDayList =  companyDaySummaryMapper.getCompanyDaySummaryByDate(date);
        if(CollectionUtils.isNotEmpty(companyDayList)){
            for (int i = 0; i < companyDayList.size(); i++) {
                CompanyDaySummaryEntity companyDaySummary = companyDayList.get(i);
                Integer totalAnswerCount = userXQuestionsMapper.getTotalAnsweredByCompanyIdAndDate(companyDaySummary.getCompanyId(), date);
                companyDaySummary.setStudyCount(totalAnswerCount);
                if(companyDaySummary.getStudyCount()==null || (totalAnswerCount>0 && !companyDaySummary.getStudyCount().equals(totalAnswerCount))){
                    updateCompanyDayList.add(companyDaySummary);
                }
            }
            if(CollectionUtils.isNotEmpty(updateCompanyDayList)) {
                logger.info("批量更新公司答题数");
                companyDaySummaryMapper.batchUpdate(updateCompanyDayList);
            }
        }
        logger.info("日期："+date+":"+companyDayList.size()+"家公司的数据已更新！");
        return ResultJson.succResultJson(companyDayList.size());
    }

    public static void main(String[] args) {
        List<CompanyDaySummaryEntity> updateCompanyDayList = new ArrayList<>();
        updateCompanyDayList.add(new CompanyDaySummaryEntity(1,1,1,1,new Date(),new Date(),new Date()));
        updateCompanyDayList.add(new CompanyDaySummaryEntity(2,2,2,2,new Date(),new Date(),new Date()));
        updateCompanyDayList.add(new CompanyDaySummaryEntity(3,3,3,3,new Date(),new Date(),new Date()));
        if(CollectionUtils.isNotEmpty(updateCompanyDayList)) {
            System.out.println(updateCompanyDayList.size());
        }
    }
}
