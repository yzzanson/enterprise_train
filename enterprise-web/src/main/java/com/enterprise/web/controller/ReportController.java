package com.enterprise.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.BaseController;
import com.enterprise.base.common.PageEntity;
import com.enterprise.base.common.ResultJson;
import com.enterprise.base.vo.AgentStatusVO;
import com.enterprise.base.vo.dto.BaseReportDTO;
import com.enterprise.base.vo.dto.CompanyReportDTO;
import com.enterprise.base.vo.dto.CompanyReportDetailDTO;
import com.enterprise.service.marketBuy.MarketBuyService;
import com.enterprise.service.report.ReportService;
import com.enterprise.util.AssertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/4/12 下午7:58
 */
@Controller
@RequestMapping("/report")
public class ReportController extends BaseController{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private ReportService reportService;

    @Resource
    private MarketBuyService marketBuyService;

    /**
     * 数据总览
     * */
    @RequestMapping("/totalScan.json")
    @ResponseBody
    public JSONObject totalScan() {
        try {
            JSONObject labelJson = reportService.totalScan();
            return labelJson;
        } catch (Exception e) {
            logger.error("report-getCompanyDetail异常:"+e.getMessage());
        }
        return ResultJson.succResultJson("SUCCESS", null);
    }


    /**
     * 数据总览-答题次数
     * */
    @RequestMapping("/totalDayAnswer.json")
    @ResponseBody
    public JSONObject totalDayAnswer(BaseReportDTO baseReportDTO) {
        try {
            JSONObject labelJson = reportService.totalDayAnswer(baseReportDTO);
            return labelJson;
        } catch (Exception e) {
            logger.error("report-totalDayAnswer异常:"+e.getMessage());
        }
        return ResultJson.succResultJson("SUCCESS", null);
    }

    /**
     * 数据总览-增长情况
     * */
    @RequestMapping("/totalAddCompUser.json")
    @ResponseBody
    public JSONObject totalAddCompUser(BaseReportDTO baseReportDTO) {
        try {
            JSONObject labelJson = reportService.totalAddCompUser(baseReportDTO);
            return labelJson;
        } catch (Exception e) {
            logger.error("report-totalAddCompUser异常:"+e.getMessage());
        }
        return ResultJson.succResultJson("SUCCESS", null);
    }

    @RequestMapping("/getSearchCondition.json")
    @ResponseBody
    public JSONObject getSearchCondition() {
        try {
            List<String> versionList = marketBuyService.getMarketVesion();
            versionList.add("免费版");
            List<AgentStatusVO> statusList = AgentStatusVO.getAgentStatusList();
            Map<String,Object> resultMap = new HashMap<>();
            resultMap.put("version", versionList);
            resultMap.put("status",statusList);
            return ResultJson.succResultJson(resultMap);
        } catch (Exception e) {
            logger.error("report-getCompanySummaryList异常:"+e.getMessage());
        }
        return ResultJson.succResultJson("SUCCESS", null);
    }


    /**
     * 客户管理-列表
     * */
    @RequestMapping("/getCompanySummaryList.json")
    @ResponseBody
    public JSONObject getCompanySummaryList(CompanyReportDTO companyReportDTO,PageEntity pageEntity) {
        try {
            JSONObject labelJson = reportService.getCompanySummaryList(companyReportDTO, pageEntity);
            return labelJson;
        } catch (Exception e) {
            logger.error("report-getCompanySummaryList异常:"+e.getMessage());
        }
        return ResultJson.succResultJson("SUCCESS", null);
    }

    /**
     * 客户管理-公司详情
     * */
    @RequestMapping("/getCompanyDetail.json")
    @ResponseBody
    public JSONObject getCompanyDetail(CompanyReportDetailDTO companyReportDetailDTO) {
        try {
            AssertUtil.notNull(companyReportDetailDTO.getCompanyId(), "公司id不能为空");
            JSONObject labelJson = reportService.getCompanyDetail(companyReportDetailDTO);
            return labelJson;
        } catch (Exception e) {
            logger.error("report-getCompanyDetail异常:"+e.getMessage());
        }
        return ResultJson.succResultJson("SUCCESS", null);
    }




}
