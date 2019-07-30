package com.enterprise.service.question;

import com.enterprise.base.entity.WeekRankEntity;
import com.enterprise.base.vo.UserAnswerRankSimpleNewVO;

import java.util.List;

/**
 * @Description UserService
 * @Author anson
 * @Date 2018/3/26 下午14:47
 */
public interface WeekRankService {

    Integer batchInsert(List<WeekRankEntity> weekRankList);

    Integer singleDelete(Integer companyId,String date);

    List<UserAnswerRankSimpleNewVO> getWeekRankByCompanyId(Integer companyId,String date);
}
