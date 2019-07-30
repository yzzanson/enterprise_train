package com.enterprise.service.question;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.PageEntity;
import com.enterprise.base.entity.UserXQuestionsEntity;
import com.enterprise.base.vo.UserAnswerRankSimpleNewVO;
import com.enterprise.base.vo.dto.QuestionAnswerDto;

import java.util.List;

/**
 * Created by anson on 18/4/2.
 */
public interface UserXQuestionsService {

    Integer createOrUpdateUserXQestion(UserXQuestionsEntity userXQuestionsEntity);

    JSONObject answer(QuestionAnswerDto questionAnswerDto,Integer effedcId);

    JSONObject reviewAnswer(QuestionAnswerDto questionAnswerDto);

    JSONObject answerTest(Integer companyId,Integer userId,Integer questionId,String answer,Integer type);

    /**
     * 首页排行榜
     * */
    JSONObject getWeekAnswerRankList(PageEntity pageEntity);

    /**
     * 学习排行
     * */
    JSONObject getStudyRank(Integer status,String search,PageEntity pageEntity);

    /**
     * 学习排行详情
     * */
    JSONObject getStudyRankDetail(Integer type,Integer userId);

    /**
     * 题库热度
     * */
    JSONObject getLibraryHeatRank(Integer type,String search,PageEntity pageEntity);

    /**
     * 分享排行榜
     * */
    JSONObject getWeekAnswerRankShare(String corpId,PageEntity pageEntity);


    /**
     * 首页排行榜
     * */
    JSONObject getMyRank(String corpId);

    /**
     * 获取企业某日答题总数
     * */
    Integer getTotalAnsweredByCompanyIdAndDate(Integer companyId,String date);

    /**
     * 同步企业所有的用户题库答题进度
     * */
    Integer synchronizeLibrary(String corpId);

    /**
     * 更新用户学习题库状态
     * */
    Integer batchUpdateStatus(Integer questionId,Integer status);

    /**
     * 获取前五排行
     * */
    List<UserAnswerRankSimpleNewVO> getTop5Rank(Integer companyId);

    /**
     * 获取回答过某题的用户列表-正确答题的才记录进答题记录
     * */
    List<UserXQuestionsEntity> getCorrectAnswerUserList(Integer companyId,Integer questionId);

    /**
     * 最近一个小时用户
     * */
    JSONObject getHourUserAnswerList();
}
