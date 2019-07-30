package com.enterprise.service.rank;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/9/17 上午11:09
 */
public interface WeekRankOpenService {

    /**
     * 本周是否打开过
     * */
    Integer checkIsOpen();

    /**
     * 打开周排行
     * */
    Integer openWeekRank(Integer companyId,Integer userId);

}
