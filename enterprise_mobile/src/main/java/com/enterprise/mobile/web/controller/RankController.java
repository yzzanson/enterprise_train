package com.enterprise.mobile.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.BaseController;
import com.enterprise.base.common.MobileLoginUser;
import com.enterprise.base.common.PageEntity;
import com.enterprise.base.common.ResultJson;
import com.enterprise.base.entity.CompanyInfoEntity;
import com.enterprise.base.entity.WeekRankEntity;
import com.enterprise.base.vo.UserAnswerRankSimpleNewVO;
import com.enterprise.base.vo.UserXTitleVO;
import com.enterprise.service.company.CompanyInfoService;
import com.enterprise.service.question.UserXQuestionsService;
import com.enterprise.service.question.WeekRankService;
import com.enterprise.service.user.UserXTitleService;
import com.enterprise.util.DateUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description 排行榜
 * @Author zezhouyang
 * @Date 18/4/2 下午4:32
 */
@Controller
@RequestMapping("/rank")
public class RankController extends BaseController {

    @Resource
    private UserXQuestionsService userXQuestionsService;

    @Resource
    private WeekRankService weekRankService;


    @Resource
    private CompanyInfoService companyInfoService;

    @Resource
    private UserXTitleService userXTitleService;


    /**
     * 首页排行
     * */
    @ResponseBody
    @RequestMapping(value="/getWeekAnswerRankList.json",method= RequestMethod.GET)
    public JSONObject getWeekAnswerRankList(PageEntity pageEntity){
        try {
            return userXQuestionsService.getWeekAnswerRankList(pageEntity);
        }catch (Exception e){
            return ResultJson.errorResultJson(e.getMessage());
        }
    }

    /**
     * 分享数据列表
     * */
    @ResponseBody
    @RequestMapping(value="/getWeekAnswerRankShare.json",method= RequestMethod.GET)
    public JSONObject getWeekAnswerRankShare(String corpId,PageEntity pageEntity){
        return userXQuestionsService.getWeekAnswerRankShare(corpId, pageEntity);
    }


    /**
     * 我的排名
     * */
    @ResponseBody
    @RequestMapping(value="/getMyRank.json",method= RequestMethod.GET)
    public JSONObject getMyRank(String corpId){
        try {
            return userXQuestionsService.getMyRank(corpId);
        }catch (Exception e){
            return ResultJson.errorResultJson(e.getMessage());
        }
    }




    /**
     * 上周排名前五
     * */
    @ResponseBody
    @RequestMapping(value="/getWeekRank.json",method= RequestMethod.GET)
    public JSONObject getWeekRank(Integer companyId){
        if(companyId==null){
            MobileLoginUser mobileLoginUser = MobileLoginUser.getUser();
            companyId = mobileLoginUser.getCompanyID();
        }
        String firstDayOfWeek = DateUtil.getDateYMD(DateUtil.getFirstDayOfWeek(new Date()));
        List<UserAnswerRankSimpleNewVO> userRankList = weekRankService.getWeekRankByCompanyId(companyId,firstDayOfWeek);
        return ResultJson.succResultJson(userRankList);
    }


    /**
     * 上周排名前五
     * */
    @ResponseBody
    @RequestMapping(value="/genRank.json",method= RequestMethod.GET)
    public JSONObject genRank(){
        String weekStartDay = DateUtil.getDateYMD(DateUtil.getFirstDayOfWeek(new Date()));
        String LastweekStartDay = DateUtil.getDateYMD(DateUtil.getFirstDayOfWeek(DateUtil.getSeveralDaysLaterDate(new Date(), -7)));
        List<CompanyInfoEntity> companyList = companyInfoService.getAllCompanys();
        for (int i = 0; i < companyList.size(); i++) {
            CompanyInfoEntity companyInfo = companyList.get(i);
            Integer companyId = companyInfo.getId();
            //删除上上周的,保留上周的
            weekRankService.singleDelete(companyId,LastweekStartDay);
            List<UserAnswerRankSimpleNewVO> userRankList = userXQuestionsService.getTop5Rank(companyInfo.getId());
            List<WeekRankEntity> weeRankList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(userRankList)) {
                for (UserAnswerRankSimpleNewVO userAnswerRankSimpleVO : userRankList) {
                    //public WeekRankEntity(Integer companyId, Integer userId, Integer rank, String dateTime) {
                    WeekRankEntity weekRankEntity = new WeekRankEntity(companyId, userAnswerRankSimpleVO.getUserId(), userAnswerRankSimpleVO.getRank(), weekStartDay);
                    UserXTitleVO userTitle = userXTitleService.findUserWearTitleByCompany(companyId, userAnswerRankSimpleVO.getUserId());
                    if(userTitle!=null){
                        weekRankEntity.setTitle(userTitle.getTitle());
                        weekRankEntity.setTitleType(userTitle.getType());
                    }
                    weekRankEntity.setWeight(userAnswerRankSimpleVO.getWeight());
                    weeRankList.add(weekRankEntity);
                }
                weekRankService.batchInsert(weeRankList);
            }
        }
        return ResultJson.succResultJson("success");
    }

    public static void main(String[] args) {
        String firstDayOfWeek = DateUtil.getDateYMD(DateUtil.getFirstDayOfWeek(new Date()));
        System.out.println(firstDayOfWeek);
    }

}
