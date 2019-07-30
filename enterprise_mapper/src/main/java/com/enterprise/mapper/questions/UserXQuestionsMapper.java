package com.enterprise.mapper.questions;

import com.enterprise.base.entity.UserXQuestionsEntity;
import com.enterprise.base.vo.UserAnswerRankVO;
import com.enterprise.base.vo.UserCompanyLibraryVO;
import com.enterprise.base.vo.UserVO3;
import com.enterprise.base.vo.rank.QuestionLibraryHeatRank;
import com.enterprise.base.vo.rank.UserPetWeightRankVO;
import com.enterprise.base.vo.rank.UserRankVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by null on 2018-03-23 11:26:25
 */
@Repository
public interface UserXQuestionsMapper {

    Integer createOrUpdateUserXQuestion(UserXQuestionsEntity userXQuestionsEntity);

    Integer createOrUpdateUserXQuestionNew(UserXQuestionsEntity userXQuestionsEntity);

    Integer updateUserXQuestion(UserXQuestionsEntity userXQuestionsEntity);

//    Integer getTotalQuestionCount(@Param("questionId") Integer questionId,@Param("answerStatus") Integer answerStatus);

//    Integer getTotalQuestionAllCount(@Param("questionList") List<Integer> questionList,@Param("answerStatus") Integer answerStatus);

    Integer isUserAnswered(@Param("userId") Integer userId);

//    List<UserAnswerRankVO> getWeekAnswerRankList(@Param("companyId") Integer companyId,@Param("startTime") Date startTime,@Param("endTime") Date endTime);

    //新的排序
    List<UserAnswerRankVO> getWeekAnswerRankList2(@Param("companyId") Integer companyId,@Param("corpId") String corpId,@Param("startTime") Date startTime,@Param("endTime") Date endTime,@Param("petUserList") List<Integer> petUserList,@Param("userOpenList") List<Integer> userOpenList,@Param("remainUserList") List<Integer> remainUserList);
    //新的排序
    List<UserPetWeightRankVO> getWeekAnswerRankList3(@Param("companyId") Integer companyId,@Param("corpId") String corpId,@Param("petUserList") List<Integer> petUserList,@Param("remainUserList") List<Integer> remainUserList);

    /**
     * 用户学习排行
     * */
    List<UserRankVO> getStudyRank(@Param("companyId") Integer companyId,@Param("status") Integer status,@Param("search") String search);

    /**
     * 用户的题库列表
     * */
    List<Integer> getUserLibraryList(@Param("companyId") Integer companyId,@Param("userId") Integer userId,@Param("type") Integer type);

    /**
     * 单个题库学习次数
     * */
    Integer getStudyCountByLibraryId(@Param("companyId") Integer companyId,@Param("libraryId") Integer libraryId,@Param("userId") Integer userId);

    /**
     * 单个题库掌握知识点个数
     * */
    Integer getRightStudyCountByLibraryId(@Param("companyId") Integer companyId,@Param("libraryId") Integer libraryId,@Param("userId") Integer userId);

    /**
     * 题库热度
     * */
    List<QuestionLibraryHeatRank> getQuestionLibraryByStudyCount(@Param("companyId") Integer companyId,@Param("type") Integer type,@Param("search") String search);

    /**
     * 所有回答正确的数目
     * */
    Integer getTotalCorrectCountByLibraryId(@Param("companyId") Integer companyId,@Param("libraryId") Integer libraryId,@Param("userId") Integer userId);

    List<Integer> getwrongAnsweredQuestions(@Param("companyId") Integer companyId,@Param("libraryId") Integer libraryId,@Param("userId") Integer userId);

    List<UserXQuestionsEntity> getIsAnswerUser(@Param("questionList") List<Integer> questionList);

    /**
     * 获取一道未答过的新题
     * */
    Integer getNewQuesion(@Param("companyId") Integer companyId,@Param("libraryId") Integer libraryId,@Param("userId") Integer userId,@Param("type") Integer type);

    /**
     * 判断该题是否为错题(即第二次出现的错题)

    Integer getIsAnsweredWrongBefore(@Param("id") Integer id,@Param("questionId") Integer questionId,@Param("userId") Integer userId);
     * */

    /**
     * 判断该题之前是都回答正确过
     * */
    Integer getIsCorrectedAnsweredBefore(@Param("companyId") Integer companyId,@Param("questionId") Integer questionId,@Param("userId") Integer userId);

    /**
     * 获取该公司所有答题数
     * */
    Integer getTotalAnsweredByCompanyIdAndDate(@Param("companyId") Integer companyId,@Param("date") String date);

    /**
     * 获取所有答题数
     * */
    Integer getTotalAnsweredByDate(@Param("date") String date);

    /**
     * 获取所有答题数用户数
     *
    Integer getTotalAnsweredUserByDate(@Param("date") String date);
     */

    /**
     * 获取该公司在某个时间段的答题数
     * */
    Integer getAnsweredByCompanyIdAndDate(@Param("companyId") Integer companyId,@Param("startDate") Date startDate,@Param("endDate") Date endDate);

    /**
     * 更新某题回答状态
     * */
    Integer batchUpdateStatus(@Param("questionId") Integer questionId,@Param("status") Integer status);

    /**
     * 获取个人周排行顺序
    UserAnswerRankVO getMyWeekAnswerRank(@Param("companyId") Integer companyId,@Param("userId") Integer userId,@Param("startDate") Date startDate,@Param("endDate") Date endDate);
     * */

    /**
     * 获取用户某个题库下自主答题的所有正确题目id
     * */
    Integer getUserCorrectQuestionsByLibraryId(@Param("companyId") Integer companyId,@Param("userId") Integer userId,@Param("libraryId") Integer libraryId);

    /**
     * 某个人在某组织下时间段内答过题
     * */
    List<Integer> getAnsweredUserList(@Param("companyId") Integer companyId,@Param("corpId") String corpId,@Param("startDate") Date startDate,@Param("endDate") Date endDate);
    /**
     * 某个人在某组织下时间段内答过题
     * */
    List<Integer> getAnsweredUserList2(@Param("companyId") Integer companyId,@Param("corpId") String corpId);

    /**
     * 获取回答正确某些题的用公司\用户
     * */
    List<UserXQuestionsEntity> getAnsweredUserListGroup(@Param("questionList") List<Integer> questionList);


    /**
     * 公司,用户答题
     * */
    List<UserCompanyLibraryVO> getAnsweredUserListByLibraryId(@Param("libraryId") Integer libraryId);


    /**
     * 获取某人某天所有正确答对的题目数
     *
    Integer getUserTotalCorrectAnswerCount(@Param("companyId") Integer companyId,@Param("userId") Integer userId,@Param("date") String date);
     */

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * 同步用
     * */
    Integer getTotalCountInDB();

    /**
     * 同步用
     * */
    List<UserXQuestionsEntity> getCountRecord(@Param("start") Integer start,@Param("end") Integer end);


    Integer batchUpdate(@Param("userXQuestionList") List<UserXQuestionsEntity> userXQuestionList);

    /**
     * 答题过的用户数
     * */
    Integer getTotalAnswerUserCount(@Param("companyId") Integer companyId,@Param("date") String date);


    /**
     * 正确回答过的用户
     * */
    List<UserXQuestionsEntity> getCorrectAnswerUserList(@Param("companyId") Integer companyId,@Param("questionId") Integer questionId);


    /**
     * 用户对某个题目的一次正确答题人数
     * */
    Integer getCorrectCount(@Param("companyId") Integer companyId,@Param("questionId") Integer questionId);

    /**
     * 题目回答人次
     * */
    Integer getQuestionAnswerUserCount(@Param("companyId") Integer companyId,@Param("questionId") Integer questionId);

    List<UserVO3> getWrongAnswerList(@Param("companyId") Integer companyId,@Param("questionId") Integer questionId);

    /**
     * 题目所有回答次数
     * */
    Integer getQuestionAnswerCount(@Param("companyId") Integer companyId,@Param("questionId") Integer questionId);

    /**
     * 所有回答错误过的题
     * */
    List<Integer> getUserwrongAnsweredQuestionList(@Param("companyId") Integer companyId,@Param("libraryId") Integer libraryId,@Param("userId") Integer userId);

    /**
     * 某个用户某题回答错误次数
     * */
    Integer getWrongAnswerCountByUserId(@Param("companyId") Integer companyId,@Param("questionId") Integer questionId,@Param("userId") Integer userId);

    /**
     * 最近一小时的用户
     * */
    List<String> getHourUserAnswerList(@Param("gainCount") Integer gainCount);

    /**
     * 最后50条
     * */
    List<String> getHourUserAnswerList2(@Param("gainCount") Integer gainCount);

    /**
     * 最后答题时间
     * */
    UserXQuestionsEntity getLastAnswerTime(@Param("companyId") Integer companyId,@Param("libraryId") Integer libraryId,@Param("userId") Integer userId);
}
