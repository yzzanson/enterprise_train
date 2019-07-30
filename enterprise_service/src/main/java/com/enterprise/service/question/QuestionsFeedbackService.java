package com.enterprise.service.question;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.PageEntity;
import com.enterprise.base.entity.QuestionsFeedbackEntity;

/**
 * @Description UserService
 * @Author anson
 * @Date 2018/3/26 下午14:47
 */
public interface QuestionsFeedbackService {

    Integer createOrUpdateQuestionFeedback(QuestionsFeedbackEntity questionsFeedbackEntity);

    Integer doUpdate(Integer id,Integer userId,Integer status);

    JSONObject findFeedbackList(Integer questionId,PageEntity pageEntity);

}
