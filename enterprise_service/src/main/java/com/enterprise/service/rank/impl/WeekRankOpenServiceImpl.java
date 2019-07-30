package com.enterprise.service.rank.impl;

import com.enterprise.base.common.MobileLoginUser;
import com.enterprise.base.entity.WeekRankOpenEntity;
import com.enterprise.mapper.rank.WeekRankOpenMapper;
import com.enterprise.service.rank.WeekRankOpenService;
import com.enterprise.util.DateUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/9/17 上午11:15
 */
@Service
public class WeekRankOpenServiceImpl implements WeekRankOpenService{

    @Resource
    private WeekRankOpenMapper weekRankOpenMapper;

    @Override
    public Integer checkIsOpen() {
        MobileLoginUser mobileLoginUser = MobileLoginUser.getUser();
        Integer companyId = mobileLoginUser.getCompanyID();
        Integer userId = mobileLoginUser.getUserID();
        Date weekStartDay = DateUtil.getWeekStartTime();
        Date weekEndDay = DateUtil.getWeekEndTime();
        WeekRankOpenEntity weekRankOpenEntity = weekRankOpenMapper.getWeekRankOpenByCompanyIdAndUserId(companyId,userId);
        if(weekRankOpenEntity!=null && weekRankOpenEntity.getWeekTime().getTime()>= weekStartDay.getTime() && weekRankOpenEntity.getWeekTime().getTime()<=weekEndDay.getTime()){
            return 1;
        }
        return 0;
    }

    @Override
    public Integer openWeekRank(Integer companyId, Integer userId) {
        WeekRankOpenEntity weekRankOpenEntity = weekRankOpenMapper.getWeekRankOpenByCompanyIdAndUserId(companyId,userId);
        Integer result = 0;
        if(weekRankOpenEntity==null){
            weekRankOpenEntity = new WeekRankOpenEntity(companyId,userId,new Date(),new Date(),new Date());
            result = weekRankOpenMapper.insertOne(weekRankOpenEntity);
        }else{
            weekRankOpenEntity = new WeekRankOpenEntity(weekRankOpenEntity.getId(),new Date(),new Date());
            result = weekRankOpenMapper.updateOne(weekRankOpenEntity);
        }
        return result;
    }

    public static void main(String[] args) {
    }
}
