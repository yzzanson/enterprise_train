package com.enterprise.mapper.rank;

import com.enterprise.base.entity.WeekRankOpenEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by null on 2018-03-23 11:26:25
 */
@Repository
public interface WeekRankOpenMapper {

    Integer insertOne(WeekRankOpenEntity weekRankOpenEntity);

    Integer updateOne(WeekRankOpenEntity weekRankOpenEntity);

    WeekRankOpenEntity getWeekRankOpenByCompanyIdAndUserId(@Param("companyId") Integer companyId,@Param("userId") Integer userId);
}