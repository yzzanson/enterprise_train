package com.enterprise.service.report;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.entity.CompanyDaySummaryEntity;

/**
 * @Description 公司每日数据汇总
 * @Author zezhouyang
 * @Date 18/4/13 下午3:33
 */
@Deprecated
public interface CompanyDaySummaryService {

    Integer createCompanyDaySummary(CompanyDaySummaryEntity companyDaySummaryEntity);

    Integer updateCompanySummary(CompanyDaySummaryEntity companyDaySummaryEntity);

    JSONObject updateCompanyDaySummary(String date);
}
