package com.enterprise.service.report;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.PageEntity;
import com.enterprise.base.vo.dto.BaseReportDTO;
import com.enterprise.base.vo.dto.CompanyReportDTO;
import com.enterprise.base.vo.dto.CompanyReportDetailDTO;

/**
 * @Description 后台统计数据
 * @Author zezhouyang
 * @Date 18/4/13 上午9:53
 */
public interface ReportService {

    JSONObject getCompanySummaryList(CompanyReportDTO companyReportDTO, PageEntity pageEntity);

    JSONObject getCompanyDetail(CompanyReportDetailDTO companyReportDetailDTO);

    JSONObject totalScan();

    JSONObject totalDayAnswer(BaseReportDTO baseReportDTO);

    JSONObject totalAddCompUser(BaseReportDTO baseReportDTO);

}
