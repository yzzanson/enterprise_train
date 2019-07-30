package com.enterprise.mapper.report;

import com.enterprise.base.entity.CompanyDaySummaryEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by null on 2018-03-23 10:45:39
 */
@Repository
public interface CompanyDaySummaryMapper {

    Integer createCompayDaySummary(CompanyDaySummaryEntity companyDaySummaryEntity);

    Integer updateCompanySummary(CompanyDaySummaryEntity companyDaySummaryEntity);

    CompanyDaySummaryEntity getCompanyDaySummaryByCompanyIdAndDate(@Param("companyId") Integer companyId,@Param("date") String date);

    List<CompanyDaySummaryEntity> getCompanyDaySummaryByDate(@Param("date") String date);

    Integer batchUpdate(@Param("updateList") List<CompanyDaySummaryEntity> updateCompanyDayList);

}