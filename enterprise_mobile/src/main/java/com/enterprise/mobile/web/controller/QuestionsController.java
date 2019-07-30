package com.enterprise.mobile.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.BaseController;
import com.enterprise.base.common.MobileLoginUser;
import com.enterprise.base.common.ResultJson;
import com.enterprise.mobile.web.interceptor.anno.TokenRequired;
import com.enterprise.service.question.QuestionsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/4/3 下午1:54
 */
@Controller
@RequestMapping("/question")
public class QuestionsController extends BaseController {

    @Resource
    private QuestionsService questionsService;

    /**
        题目出现规则：已答对的题目不再出现，未答题目每出现5道则出现一次错题；
        历史答题算
        获取某个题库下的题目
     @Param type 0/1 随机/顺序
    */
    @TokenRequired
    @RequestMapping("/getNextQuestion.json")
    @ResponseBody
    public JSONObject getNextQuestion(Integer libraryId){
        try {
            MobileLoginUser mLoginUser = MobileLoginUser.getUser();
            return questionsService.getNextQuestion(libraryId, mLoginUser.getCompanyID(), mLoginUser.getUserID());
        }catch (Exception e){
            return ResultJson.errorResultJson(e.getMessage());
        }
    }



    /**
     * 知识回顾
     * libraryId 题库id
     * questionId 问题id
     * */
    @Deprecated
    @RequestMapping("/reviewQuestion.json")
    @ResponseBody
    public JSONObject reviewQuestion(Integer libraryId,Integer questionId){
        return questionsService.reviewQuestion(libraryId,questionId);
    }

    /**
     * 知识回顾,上一题
     * libraryId 题库id
     * questionId 问题id
     * */
    @Deprecated
    @RequestMapping("/preReviewQuestion.json")
    @ResponseBody
    public JSONObject preReviewQuestion(Integer libraryId,Integer questionId){
        return questionsService.preReviewQuestion(libraryId, questionId);
    }

    /**
     * 跳过题目

    @Deprecated
    @RequestMapping("/skipQuesion.json")
    @ResponseBody
    public JSONObject skipQuesion(Integer libraryId,Integer questionId){
        MobileLoginUser mLoginUser = MobileLoginUser.getUser();
        return questionsService.skipQuesion(libraryId,mLoginUser.getCompanyID(),mLoginUser.getUserID(),questionId);
    }
     * */

}
