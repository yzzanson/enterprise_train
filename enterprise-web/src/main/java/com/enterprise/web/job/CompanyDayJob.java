package com.enterprise.web.job;

import com.enterprise.base.entity.CompanyDaySummaryEntity;
import com.enterprise.base.entity.CompanyInfoEntity;
import com.enterprise.oa.OAMessageUtil;
import com.enterprise.service.company.CompanyInfoService;
import com.enterprise.service.question.UserXQuestionsService;
import com.enterprise.service.report.CompanyDaySummaryService;
import com.enterprise.service.report.UserXOpenService;
import com.enterprise.service.user.UserService;
import com.enterprise.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 每日0点执行
 * 更新企业昨日相比前日
 * @Description
 * @Author zezhouyang
 * @Date 18/4/13 上午11:01
 */
@Deprecated
public class CompanyDayJob {

    private static final Logger logger = LoggerFactory.getLogger(CompanyDayJob.class);

    @Resource
    private UserXOpenService userXOpenService;

    @Resource
    private UserXQuestionsService userXQuestionsService;

    @Resource
    private CompanyInfoService companyInfoService;

    @Resource
    private UserService userService;

    @Resource
    private CompanyDaySummaryService companyDaySummaryService;

    /**
     * 获取昨天的数据
     * */
    public void work() {
        List<CompanyInfoEntity> companyList = companyInfoService.getAllCompanys();
        for (int i = 0; i < companyList.size(); i++) {
            CompanyInfoEntity companyInfo = companyList.get(i);
            Integer companyId = companyInfo.getId();
            CompanyDaySummaryEntity companyDaySummary = new CompanyDaySummaryEntity();
            companyDaySummary.setCompanyId(companyId);
            String yesterDay = DateUtil.getDateYMD(DateUtil.getDateBeforeHourTime(new Date(), 24));
            Date yesterDayBegin = DateUtil.getDateStartDateTime(DateUtil.getDateBeforeHourTime(new Date(), 24));
            if(!DateUtil.isValidDate(yesterDay)){
                continue;
            }
            Integer enterCount = userXOpenService.getCountByCompanyId(companyId, yesterDay);
            Integer studycount = userXQuestionsService.getTotalAnsweredByCompanyIdAndDate(companyId, yesterDay);
            Integer newUserCount = userService.getNewUserByCompanyIdAndDate(companyId, yesterDay);
            CompanyDaySummaryEntity companyDaySummaryEntity = new CompanyDaySummaryEntity();
            companyDaySummaryEntity.setCompanyId(companyId);
            companyDaySummaryEntity.setEnterCount(enterCount);
            companyDaySummaryEntity.setStudyCount(studycount);
            companyDaySummaryEntity.setNewUserCount(newUserCount);
            companyDaySummaryEntity.setDate(yesterDayBegin);
            companyDaySummaryEntity.setCreateTime(new Date());
            companyDaySummaryEntity.setUpdateTime(new Date());
            if(companyDaySummaryService.createCompanyDaySummary(companyDaySummaryEntity)<=0){
                logger.error("保存公司"+companyId+"昨日数据失败!");
            }
        }
        OAMessageUtil.sendTextMsgToDept(getMessageContent(companyList.size()));

    }

    private String getMessageContent(Integer companyCount){
        StringBuffer sb = new StringBuffer();
        sb.append("更新学习总数结束"+"\n");
        sb.append("公司总数："+companyCount+"\n");
        sb.append("更新时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "\n");
        return sb.toString();
    }

}
