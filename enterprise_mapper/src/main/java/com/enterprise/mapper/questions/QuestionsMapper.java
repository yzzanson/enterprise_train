package com.enterprise.mapper.questions;

import com.enterprise.base.entity.QuestionsEntity;
import com.enterprise.base.vo.QuestionVO;
import com.enterprise.base.vo.QuestionVO1;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by null on 2018-03-23 10:43:28
 */
public interface QuestionsMapper {

    Integer createOrUpdateQuestion(QuestionsEntity questionsEntity);

    /**
     * 取某一题库的题目总数
     * */
    Integer getTotalCountByLibraryId(@Param("libraryId")Integer libraryId);

    /**
     * 通过问题获取所有子题目列表
     * */
    List<Integer> getQuestionIdsByParent(@Param("parentId")Integer parentId);

    /**
     * 使用题库复制题库的题目
     * */
    Integer insertQuestionsFromOtherLibrary(@Param("fromLibraryId")Integer fromLibraryId,@Param("toLibraryId")Integer toLibraryId,@Param("userId")Integer userId);

    /**
     * 获取题库下的题目列表,概况
     * */
    List<QuestionVO> getQuestionList(@Param("libraryId")Integer libraryId,@Param("description")String description,@Param("labelList") List<String> labelList);

    Integer batchDelete(@Param("ids")List<Integer> ids);

    QuestionsEntity getById(@Param("id") Integer id);

    Integer updateQuestion(QuestionsEntity questionsEntity);

    /**
     * 获取问题详情列表
     * */
    List<QuestionVO1> getQuestionDetailList(@Param("libraryId")Integer libraryId);

    QuestionsEntity getNewBieQuestion(@Param("companyId")Integer companyId);
    QuestionsEntity getNewBieQuestion2(@Param("companyId")Integer companyId);

    /**
     * 从题库中随机取x条数据
     * */
    List<Integer> getQuestionIdsByLibrary(@Param("libraryId")Integer libraryId,@Param("count")Integer count);

    /**
     * 从题库中随机取x条数据
     * */
    List<Integer> getOrderQuestionIdsByLibrary(@Param("libraryId")Integer libraryId);

    /**
     * 批量导入数据
     * */
    Integer batchInsertQuestions(@Param("questionlist") List<QuestionsEntity> list);

    /**
     * 获取题库下的所有题目
     * */
    List<QuestionsEntity> getAllFromLibrary(@Param("libraryId")Integer libraryId);

    /**
     * 移除具有该标签的题目的标签
     * */
    Integer removeLabels(@Param("labelIds")List<Integer> labelIds);

    /**
     * 获取是否有使用该标签的题目
     * */
    Integer getQuestionCountByLabelId(@Param("labelId") Integer labelId);

}
