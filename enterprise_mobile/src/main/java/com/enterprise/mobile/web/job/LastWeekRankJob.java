package com.enterprise.mobile.web.job;

import com.enterprise.base.entity.CompanyInfoEntity;
import com.enterprise.base.entity.PetWeightEntity;
import com.enterprise.base.entity.WeekRankEntity;
import com.enterprise.base.vo.UserAnswerRankSimpleNewVO;
import com.enterprise.base.vo.UserXTitleVO;
import com.enterprise.mapper.pet.PetWeightMapper;
import com.enterprise.service.company.CompanyInfoService;
import com.enterprise.service.question.UserXQuestionsService;
import com.enterprise.service.question.WeekRankService;
import com.enterprise.service.user.UserXTitleService;
import com.enterprise.util.DateUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/9/4 下午4:31
 */
@Component
public class LastWeekRankJob {

    @Resource
    private CompanyInfoService companyInfoService;

    @Resource
    private UserXQuestionsService userXQuestionsService;

    @Resource
    private WeekRankService weekRankService;

    @Resource
    private UserXTitleService userXTitleService;

    @Resource
    private PetWeightMapper petWeightMapper;

    //持久化上周的排行榜数据
    public void work() {
        //获取本周一的日期
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
                    PetWeightEntity petWeightEntity = petWeightMapper.getPetWeight(companyId, userAnswerRankSimpleVO.getUserId());
                    if(weekRankEntity!=null){
                        petWeightEntity.setWeight(petWeightEntity.getWeight());
                    }else{
                        petWeightEntity.setWeight(0);
                    }
                    weeRankList.add(weekRankEntity);
                }
                weekRankService.batchInsert(weeRankList);
            }
        }
    }

    public static void main(String[] args) {
        String weekStartDay = DateUtil.getDateYMD(DateUtil.getFirstDayOfWeek(new Date()));
        String LastweekStartDay = DateUtil.getDateYMD(DateUtil.getFirstDayOfWeek(DateUtil.getSeveralDaysLaterDate(new Date(), -7)));
        System.out.println(weekStartDay);
        System.out.println(LastweekStartDay);

    }

}
