package com.enterprise.service.question;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.MobileLoginUser;
import com.enterprise.base.common.PageEntity;
import com.enterprise.base.entity.QuestionsEntity;

import java.io.File;

/**
 * @Description UserService
 * @Author anson
 * @Date 2018/3/26 下午14:47
 */
public interface QuestionsService {

    Integer createOrUpdateQuestion(QuestionsEntity questionsEntity);

    /**
     * 批量重新学习
     * */
    JSONObject batchRestudyQuestion(String ids);

    JSONObject getQuestionList(Integer libraryId,String name,String label,PageEntity pageEntity);

    JSONObject batchDelete(String ids);

    QuestionsEntity getById(Integer id);

    JSONObject getQuestionDetailList(Integer libraryId,PageEntity pageEntity);

    JSONObject getNewBieQuestion(MobileLoginUser mobileLoginUser);

    JSONObject getNextQuestion(Integer libraryId,Integer companyId,Integer userId);

    JSONObject getNextQuestionTest(Integer libraryId,Integer companyId,Integer userId);

    JSONObject getQuestionDetailById(Integer id);

    /**
     * @Pararm file 文件
     * @Pararm libraryId 题库id
     * @Pararm label 标签
     * */
    JSONObject importQuestions(File file,Integer libraryId);

    Integer getQuestionCount(Integer libraryId);

    /**
     * 回顾问题
     * */
    JSONObject reviewQuestion(Integer libraryId,Integer questionId);

    /**
     * 回顾问题
     * */
    JSONObject preReviewQuestion(Integer libraryId,Integer questionId);


}

