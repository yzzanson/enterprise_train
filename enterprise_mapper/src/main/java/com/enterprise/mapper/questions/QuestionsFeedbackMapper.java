package com.enterprise.mapper.questions;

import com.enterprise.base.entity.QuestionsFeedbackEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by null on 2018-03-23 10:45:39
 */
public interface QuestionsFeedbackMapper {

    Integer createOrUpdateQuestionFeedback(QuestionsFeedbackEntity questionsFeedbackEntity);

    Integer doUpdate(@Param("id") Integer id,@Param("handlerUser") Integer handlerUser,@Param("status") Integer status);

    List<QuestionsFeedbackEntity> findFeedbackList(@Param("questionId") Integer questionId);

    /**
     * 获取某题的反馈数
     * */
    Integer getFeedbackCount(@Param("questionId") Integer questionId);
}