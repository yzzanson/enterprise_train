package com.enterprise.mapper.rank;

import com.enterprise.base.entity.RankPraiseEntity;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * Created by null on 2018-03-23 10:45:39
 */
public interface RankPraiseMapper {

    Integer createRankPraise(RankPraiseEntity rankPraiseEntity);

    Integer deleteExpireData(@Param("beginTime") Date beginTime);

    RankPraiseEntity getByCompanyAndUser(@Param("companyId") Integer companyId,@Param("userId") Integer userId,@Param("praiseUserId") Integer praiseUserId);
}