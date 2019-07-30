package com.enterprise.mapper.rank;

import com.enterprise.base.entity.WeekRankEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by null on 2018-03-23 11:26:25
 */
@Repository
public interface WeekRankMapper {

    /**
     * 删除上上礼拜数据
     * */
    Integer singleDelete(@Param("companyId") Integer companyId,@Param("date") String date);

    Integer batchInsert(@Param("weekRankList") List<WeekRankEntity> weekRankList);

    List<WeekRankEntity> getWeekRankByCompanyId(@Param("companyId") Integer companyId,@Param("date") String date);
}