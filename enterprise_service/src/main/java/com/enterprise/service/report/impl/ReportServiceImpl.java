package com.enterprise.service.report.impl;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.*;
import com.enterprise.base.entity.CompanyInfoEntity;
import com.enterprise.base.entity.IsvTicketsEntity;
import com.enterprise.base.entity.MarketBuyEntity;
import com.enterprise.base.enums.AgentStatusEnum;
import com.enterprise.base.enums.StatusEnum;
import com.enterprise.base.vo.*;
import com.enterprise.base.vo.dto.BaseReportDTO;
import com.enterprise.base.vo.dto.CompanyReportDTO;
import com.enterprise.base.vo.dto.CompanyReportDetailDTO;
import com.enterprise.base.vo.dto.DayDTO;
import com.enterprise.mapper.companyInfo.CompanyInfoMapper;
import com.enterprise.mapper.isvTickets.IsvTicketsMapper;
import com.enterprise.mapper.markeyBuy.MarketBuyMapper;
import com.enterprise.mapper.questions.UserXQuestionsMapper;
import com.enterprise.mapper.users.UserMapper;
import com.enterprise.mapper.users.UserXCompanyMapper;
import com.enterprise.service.report.ReportService;
import com.enterprise.util.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/4/13 下午3:46
 */
@Service
public class ReportServiceImpl implements ReportService {

    @Resource
    private CompanyInfoMapper companyInfoMapper;

    @Resource
    private IsvTicketsMapper isvTicketsMapper;

    @Resource
    private UserXCompanyMapper userXCompanyMapper;

    @Resource
    private UserXQuestionsMapper userXQuestionsMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private MarketBuyMapper marketBuyMapper;

//    private Integer DAY_SECONDS = 3600 * 24;
//    private Integer HOUR_SECONDS = 3600;
//    private Integer SECOND_SECONDS = 60;

    private static final String OPER_CORPID = GlobalConstant.getOperCorpId();

    private static final String DEFAULT_VERSION = GlobalConstant.DEFAULT_VERSION;

    private static final Integer DEFAULT_MAX_FREE = 99;

    @Override
    public JSONObject getCompanySummaryList(CompanyReportDTO companyReportDTO, PageEntity pageEntity) {
        Date startDate = null;
        Date endDate = null;
        //ding440a3cea1d4e70b135c2f4657eb6378f
        if (!LoginUser.getUser().getCorpID().equals(OPER_CORPID)) {
            companyReportDTO.setCorpId(LoginUser.getUser().getCorpID());
        }

        if (StringUtils.isNotEmpty(companyReportDTO.getStartTime())) {
            startDate = DateUtil.getDate2(companyReportDTO.getStartTime());
            companyReportDTO.setStartDate(startDate);
        }
        if (StringUtils.isNotEmpty(companyReportDTO.getEndTime())) {
            endDate = DateUtil.getDate2(companyReportDTO.getEndTime());
            companyReportDTO.setEndDate(endDate);
        }
        List<Integer> agentStatusList = new ArrayList<>();
        if (StringUtils.isNotEmpty(companyReportDTO.getAgentStatus())) {
            String[] agentStatusArray = companyReportDTO.getAgentStatus().split(",");
            for (int i = 0; i < agentStatusArray.length; i++) {
                agentStatusList.add(Integer.valueOf(agentStatusArray[i]));
            }
            companyReportDTO.setAgentStatusList(agentStatusList);
        }

        Integer singleVersion = 1;
        List<String> versionList = new ArrayList<>();
        if (StringUtils.isNotEmpty(companyReportDTO.getVersion())) {
            String versionStr = companyReportDTO.getVersion();
            if (versionStr.indexOf(",") < 0) {
                if (versionStr.contains("免费版")) {
                    singleVersion = 1;
                    companyReportDTO.setVersion("1");
                } else {
                    singleVersion = 2;
                    companyReportDTO.setVersion("2");
                }
                versionList.add(versionStr);
            } else if ((versionStr.indexOf(",") > 0 && !versionStr.contains("免费版"))) {
                singleVersion = 2;
                companyReportDTO.setVersion("2");
                String[] versionArray = versionStr.split(",");
                for (int i = 0; i < versionArray.length; i++) {
                    versionList.add(versionArray[i]);
                }
            } else if ((versionStr.indexOf(",") > 0 && versionStr.contains("免费版"))) {
                singleVersion = 0;
                String[] versionArray = versionStr.split(",");
                for (int i = 0; i < versionArray.length; i++) {
                    if (!versionArray[i].equals("免费版")) {
                        versionList.add(versionArray[i]);
                    }
                }
            }
            companyReportDTO.setVersionList(versionList);
        }else{
            singleVersion = 3;
        }
        String today = DateUtil.getDateYMD(new Date());
        PageHelper.startPage(pageEntity.getPageNo(), pageEntity.getPageSize());
        PageInfo<CompanyInfoEntity> pageInfo = null;
        if (singleVersion.equals(1) || singleVersion.equals(2) || singleVersion.equals(3)) {
            pageInfo = new PageInfo<>(companyInfoMapper.getCompanysByCondition(companyReportDTO));
        } else if(singleVersion.equals(0)){
            pageInfo = new PageInfo<>(companyInfoMapper.getCompanysByConditionMultiVersion(companyReportDTO));
        }
        List<CompanySummaryVO> questionLibraryVOList = new ArrayList<>();
        for (int i = 0; i < pageInfo.getList().size(); i++) {
            CompanyInfoEntity companyInfoEntity = pageInfo.getList().get(i);
            Integer companyId = companyInfoEntity.getId();
            String companyName = companyInfoEntity.getName();
            String appStatus = companyInfoEntity.getAgentStatus() != null ? AgentStatusEnum.getAgentStatusEnum(companyInfoEntity.getAgentStatus()).getChiDesc() : "";
            IsvTicketsEntity isvTicketsEntity = isvTicketsMapper.getIsvTicketByCompanyId(companyId);
            String corpId = isvTicketsEntity.getCorpId();
            String version = getVersion(companyId);
            String installTime = DateUtil.getDisplayYMDHMS(isvTicketsEntity.getCreateTime());
            Integer userCount = userXCompanyMapper.getNewUserByCompanyIdAndDate(companyId, null);
            Integer todayAnswerCount = userXQuestionsMapper.getTotalAnsweredByCompanyIdAndDate(companyId, today);
            Integer allAnswerCount = userXQuestionsMapper.getTotalAnsweredByCompanyIdAndDate(companyId, null);
            //有效用户
            Integer activeUserCount = userXCompanyMapper.getTotalActiveUserCount(corpId);
            //答题用户
            Integer answerUserCount = userXQuestionsMapper.getTotalAnswerUserCount(companyId, null);
            activeUserCount = activeUserCount == null ? 0 : activeUserCount;
            answerUserCount = answerUserCount == null ? 0 : answerUserCount;

            String averageAnswerCount = getDivisionResult(allAnswerCount, answerUserCount);
//            Integer concatCall =  AuthHelper.checkCall(isvTicketsEntity.getCorpAccessToken());
            Integer concatCall = isvTicketsEntity.getIsCall();
            String remainDay = getAvailableDay(companyId);

            CompanySummaryVO companySummaryVO = new CompanySummaryVO();
            companySummaryVO.setCompanyId(companyId);
            companySummaryVO.setCompanyName(companyName);
            companySummaryVO.setAppStatus(appStatus);
            companySummaryVO.setVersion(version);
            companySummaryVO.setUserCount(userCount);
            companySummaryVO.setActiveUserCount(activeUserCount);
            companySummaryVO.setUserPerAnswerCount(new BigDecimal(averageAnswerCount));
            companySummaryVO.setAnswerCount(allAnswerCount);
            companySummaryVO.setTodayAnswerCount(todayAnswerCount);
            companySummaryVO.setInstallTime(installTime);
            companySummaryVO.setContactCall(concatCall);
            companySummaryVO.setAvailableDay(remainDay);
            companySummaryVO.setCorpId(isvTicketsEntity.getCorpId());
            System.out.println(companySummaryVO.toString());
            questionLibraryVOList.add(companySummaryVO);
        }
        Map<String, Object> dataMap = Maps.newHashMap();
        dataMap.put("list", questionLibraryVOList);
        dataMap.put("total", pageInfo.getTotal());
        dataMap.put("page", new Page(pageInfo.getPrePage(), pageEntity.getPageNo(), pageInfo.getNextPage(), pageInfo.getLastPage()));
        return ResultJson.succResultJson(dataMap);
    }

    private String getVersion(Integer companyId) {
        Integer modelType = GlobalConstant.modelType;
        if (modelType == 2) {
            if (companyId <= DEFAULT_MAX_FREE) {
                return DEFAULT_VERSION;
            }
            String versionName = "";
            IsvTicketsEntity isvTicketsEntity = isvTicketsMapper.getIsvTicketByCompanyId(companyId);
            List<MarketBuyEntity> marketBuyList = marketBuyMapper.getByCorpId(isvTicketsEntity.getCorpId(), StatusEnum.OK.getValue());
            if (CollectionUtils.isNotEmpty(marketBuyList)) {
                versionName = marketBuyList.get(0).getItemName();
            } else {
                versionName = "未知版本";
            }
            return versionName;
        } else {
            return DEFAULT_VERSION;
        }
    }

    private String getAvailableDay(Integer companyId) {
        String availableDay = "0天";
        Integer modelType = GlobalConstant.modelType;
        if (modelType == 2) {
            if (companyId <= DEFAULT_MAX_FREE) {
                return "999天";
            }
            IsvTicketsEntity isvTicketsEntity = isvTicketsMapper.getIsvTicketByCompanyId(companyId);
            MarketBuyEntity marketBuy = marketBuyMapper.getByCorpIdAndStatus(isvTicketsEntity.getCorpId(), StatusEnum.OK.getValue());
            if (marketBuy != null && marketBuy.getServiceStopTime() != null) {
                Date expireDay = marketBuy.getServiceStopTime();
                long remainDay = DateUtil.getDiffDays(new Date(), expireDay);
                availableDay = remainDay + "天";
            }
            return availableDay;
        }
        return availableDay;
    }

    @Override
    public JSONObject getCompanyDetail(CompanyReportDetailDTO companyReportDetailDTO) {
        Integer companyId = companyReportDetailDTO.getCompanyId();
        String companyName = companyInfoMapper.getCompanyNameById(companyId);
        IsvTicketsEntity isvTicketsEntity = isvTicketsMapper.getIsvTicketByCompanyId(companyId);
        String corpId = isvTicketsEntity.getCorpId();
        String installTime = DateUtil.getDisplayYMDHMS(isvTicketsEntity.getCreateTime());
        Integer allAnswerCount = userXQuestionsMapper.getTotalAnsweredByCompanyIdAndDate(companyId, null);
        //答题用户
        Integer answerUserCount = userXQuestionsMapper.getTotalAnswerUserCount(companyId, null);
        String averageAnswerCount = getDivisionResult(allAnswerCount, answerUserCount);

        //设置默认开始结束日期
        if (StringUtils.isNotEmpty(companyReportDetailDTO.getStartTime())) {
            companyReportDetailDTO.setStartDate(DateUtil.getDate2(companyReportDetailDTO.getStartTime()));
        } else {
            Date sevenDayBeofreYesterDayBegin = DateUtil.getDateStartDateTime(DateUtil.getBeforeDayStartDateTime(DateUtil.getDateBeforeHourTime(new Date(), 5 * 24)));
            companyReportDetailDTO.setStartDate(sevenDayBeofreYesterDayBegin);
        }
        if (StringUtils.isNotEmpty(companyReportDetailDTO.getEndTime())) {
            companyReportDetailDTO.setEndDate(DateUtil.getDate2(companyReportDetailDTO.getEndTime()));
        } else {
            companyReportDetailDTO.setEndDate(new Date());
        }
        List<CompanyAnswerPerDayVO> companyAnswerPerDayList = new ArrayList<>();
        List<NewUserVO> newUserList = new ArrayList<>();
        List<DayDTO> dayList = DateUtil.getDaysBetween(companyReportDetailDTO.getStartDate(), companyReportDetailDTO.getEndDate());
        for (DayDTO dayDTO : dayList) {
            String dateStr = DateUtil.getDateYMD(dayDTO.getStartDate());
            Integer dayAnswerCount = userXQuestionsMapper.getAnsweredByCompanyIdAndDate(companyId, dayDTO.getStartDate(), dayDTO.getEndDate());
            Integer dayAnswerUserCount = userXQuestionsMapper.getTotalAnswerUserCount(companyId, dateStr);
            dayAnswerCount = dayAnswerCount == null ? 0 : dayAnswerCount;
            dayAnswerUserCount = dayAnswerUserCount == null ? 0 : dayAnswerUserCount;
            String dayPerOpenAnswer = getDivisionResult(dayAnswerCount, dayAnswerUserCount);
            CompanyAnswerPerDayVO companyAnswerPerDay = new CompanyAnswerPerDayVO(dateStr, dayPerOpenAnswer, new BigDecimal(dayPerOpenAnswer));
            companyAnswerPerDayList.add(companyAnswerPerDay);

            //新增有效用户
            Integer addActiveUserCount = userXCompanyMapper.getNewActiveUserByCompanyIdAndDate(corpId, dateStr);
            NewUserVO newUserVO = new NewUserVO(dateStr, addActiveUserCount, null);
            newUserList.add(newUserVO);
        }

        CompanySummaryDetailVO companySummaryDetailVO = new CompanySummaryDetailVO(companyId, companyName, installTime, allAnswerCount, averageAnswerCount);
        companySummaryDetailVO.setCompanyAnswerPerDayList(companyAnswerPerDayList);
        companySummaryDetailVO.setNewUserList(newUserList);
        return ResultJson.succResultJson(companySummaryDetailVO);
    }

    @Override
    public JSONObject totalScan() {
        Integer companyCount = companyInfoMapper.getAllCompanys().size();
        Integer userCount = userXCompanyMapper.getTotalUserCount();
        Integer activeUserCount = userXCompanyMapper.getTotalActiveUserCount(null);
        Float activeRateFloat = (float) activeUserCount * 100 / userCount;
        BigDecimal activeRate = new BigDecimal(activeRateFloat).setScale(1, BigDecimal.ROUND_HALF_UP);
        Integer allAnswerCount = userXQuestionsMapper.getTotalAnsweredByDate(null);
        //不同人在不同企业算2个人
        Integer allAnswerUserCount = userXQuestionsMapper.getTotalAnswerUserCount(null, null);
        allAnswerCount = allAnswerCount == null ? 0 : allAnswerCount;
        allAnswerUserCount = allAnswerUserCount == null ? 0 : allAnswerUserCount;
        Float userPerAnswerFloat = (float) allAnswerCount / allAnswerUserCount;
        BigDecimal userPerAnswerCount = new BigDecimal(userPerAnswerFloat).setScale(1, BigDecimal.ROUND_HALF_UP);
        AllSummaryDetailVO allSummaryDetailVO = new AllSummaryDetailVO(companyCount, userCount, activeUserCount, activeRate, allAnswerCount, userPerAnswerCount);
        return ResultJson.succResultJson(allSummaryDetailVO);
    }

    @Override
    public JSONObject totalDayAnswer(BaseReportDTO baseReportDTO) {
        if (StringUtils.isNotEmpty(baseReportDTO.getStartTime())) {
            baseReportDTO.setStartDate(DateUtil.getDate2(baseReportDTO.getStartTime()));
        } else {
            Date sevenDayBeofreYesterDayBegin = DateUtil.getDateStartDateTime(DateUtil.getBeforeDayStartDateTime(DateUtil.getDateBeforeHourTime(new Date(), 5 * 24)));
            baseReportDTO.setStartDate(sevenDayBeofreYesterDayBegin);
        }
        if (StringUtils.isNotEmpty(baseReportDTO.getEndTime())) {
            baseReportDTO.setEndDate(DateUtil.getDate2(baseReportDTO.getEndTime()));
        } else {
            baseReportDTO.setEndDate(new Date());
        }
        List<DayDTO> dayList = DateUtil.getDaysBetween(baseReportDTO.getStartDate(), baseReportDTO.getEndDate());
        List<AllPerAnswerVO> allPerAnswerVOList = new ArrayList<>();
        for (DayDTO dayDTO : dayList) {
            String dayStr = DateUtil.getDateYMD(dayDTO.getStartDate());
            Integer allAnswerCount = userXQuestionsMapper.getTotalAnsweredByDate(dayStr);
            Integer dayAnswerUserCount = userXQuestionsMapper.getTotalAnswerUserCount(null, dayStr);
            allAnswerCount = allAnswerCount == null ? 0 : allAnswerCount;
            dayAnswerUserCount = dayAnswerUserCount == null ? 0 : dayAnswerUserCount;
//            Integer allAnswerCount = userXQuestionsMapper.getTotalAnsweredByDate(dayStr);
            String answerPerUser = getDivisionResult(allAnswerCount, dayAnswerUserCount);
            AllPerAnswerVO allPerAnswerVO = new AllPerAnswerVO(dayStr, allAnswerCount, dayAnswerUserCount, answerPerUser);
            allPerAnswerVOList.add(allPerAnswerVO);
        }
        return ResultJson.succResultJson(allPerAnswerVOList);
    }

    @Override
    public JSONObject totalAddCompUser(BaseReportDTO baseReportDTO) {
        if (StringUtils.isNotEmpty(baseReportDTO.getStartTime())) {
            baseReportDTO.setStartDate(DateUtil.getDate2(baseReportDTO.getStartTime()));
        } else {
            Date sevenDayBeofreYesterDayBegin = DateUtil.getDateStartDateTime(DateUtil.getBeforeDayStartDateTime(DateUtil.getDateBeforeHourTime(new Date(), 5 * 24)));
            baseReportDTO.setStartDate(sevenDayBeofreYesterDayBegin);
        }
        if (StringUtils.isNotEmpty(baseReportDTO.getEndTime())) {
            baseReportDTO.setEndDate(DateUtil.getDate2(baseReportDTO.getEndTime()));
        } else {
            baseReportDTO.setEndDate(new Date());
        }
        List<DayDTO> dayList = DateUtil.getDaysBetween(baseReportDTO.getStartDate(), baseReportDTO.getEndDate());
        List<NewUserVO> allPerAnswerVOList = new ArrayList<>();
        for (DayDTO dayDTO : dayList) {
            String dayStr = DateUtil.getDateYMD(dayDTO.getStartDate());

            Integer addActiveUserCount = userXCompanyMapper.getNewActiveUserByCompanyIdAndDate(null, dayStr);
            Integer newCompanyCount = companyInfoMapper.getCompanyCountByDate(dayStr);
            NewUserVO newUserVO = new NewUserVO(dayStr, addActiveUserCount, newCompanyCount);
            allPerAnswerVOList.add(newUserVO);
        }
        return ResultJson.succResultJson(allPerAnswerVOList);
    }


    private String getDivisionResult(Integer divide, Integer divided) {
        if (divided.equals(0)) return "0";
        float num = (float) divide / divided;
        DecimalFormat df = new DecimalFormat("0.0");
        return df.format(num);
    }

    private String getLastOpenDuration(Date lastOpenTime) {
        if (lastOpenTime == null) {
            return null;
        }
        long diff = new Date().getTime() - lastOpenTime.getTime(); // 得到的差值是微秒级别，可以忽略
        long days = diff / (1000 * 60 * 60 * 24);
        long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60) - minutes * (1000 * 60)) / (1000);
        StringBuffer stringBuffer = new StringBuffer();
        if (days > 0) {
            stringBuffer.append(days + "天");
        }
        if (hours > 0) {
            stringBuffer.append(hours + "小时");
        }
        if (minutes > 0) {
            stringBuffer.append(minutes + "分");
        }
        if (seconds > 0) {
            stringBuffer.append(seconds + "秒");
        }
        return stringBuffer.toString();
    }

    public static void main(String[] args) {
//        JSONObject object = new JSONObject();
//        object.put("name", "wei");
//        object.put("age", null);
//        object.put("date", new Date());
//        String jsonString = JSON.toJSONString(object,
//                SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat);
//        System.out.println(jsonString);

//        Integer userCount = 3;
//        Integer activeUserCount = 1 * 100;
//        Float activeRate = (float) activeUserCount / userCount;
//        BigDecimal b = new BigDecimal(activeRate).setScale(1, BigDecimal.ROUND_HALF_UP);
//        System.out.println(b);

        Date date = DateUtil.convertStrToDate("2019-01-16 13:20:00");
        Date date2 = new Date();
        long diff = DateUtil.getDiffDays(date2, date);
        System.out.println(diff);
    }
}
