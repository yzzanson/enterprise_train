package com.enterprise.mapper.users;

import com.enterprise.base.entity.UserXOpenEntity;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/4/12 下午8:13
 */
public interface UserXOpenMapper {

    Integer createUserXOpen(UserXOpenEntity userXOpenEntity);

    Integer getCountByCompanyid(@Param("companyId") Integer companyId,@Param("date") String date);

    Date getLastOpenTime(@Param("companyId") Integer companyId);

    Integer getCountByCompanyidAndMonth(@Param("companyId") Integer companyId,@Param("month") String month);

    Integer getCountByCompanyidAndTime(@Param("companyId") Integer companyId,@Param("startTime") Date startTime,@Param("endTime") Date endTime);

    List<Date> getLastSeverlOpenRecords(@Param("companyId") Integer companyId,@Param("userId") Integer userId,@Param("time") Integer time);

    /**
     * 获取排除本周答题,领养过宠物的用户登录记录
     * */
    List<Integer> getExceludeUserIdList(@Param("companyId") Integer companyId,@Param("weekUserAnswerList") List<Integer> weekUserAnswerList,@Param("petUserList") List<Integer> petUserList);
}
