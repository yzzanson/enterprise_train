package com.enterprise.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.BaseController;
import com.enterprise.base.common.LoginUser;
import com.enterprise.base.common.PageEntity;
import com.enterprise.base.common.ResultJson;
import com.enterprise.base.entity.QuestionsFeedbackEntity;
import com.enterprise.service.question.QuestionsFeedbackService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by anson on 18/3/27.
 */
@Controller
@RequestMapping("/questions_feedback")
public class QuestionsFeedbackController extends BaseController {

    @Resource
    private QuestionsFeedbackService questionsFeedbackService;

    /**
     * 新增or修改反馈
     */
    @RequestMapping(value = "/createOrUpdate.json", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject createOrUpdateQuestionLibrary(QuestionsFeedbackEntity questionsFeedbackEntity) {
        LoginUser loginUser = LoginUser.getUser();
        if (questionsFeedbackEntity.getId() == null) {
            questionsFeedbackEntity.setUserId(loginUser.getUserID());
        } else if (questionsFeedbackEntity.getId() != null) {
            questionsFeedbackEntity.setHandleUserId(loginUser.getUserID());
        }
        Integer result = questionsFeedbackService.createOrUpdateQuestionFeedback(questionsFeedbackEntity);
        if (result > 0) {
            return ResultJson.succResultJson(questionsFeedbackEntity);
        } else {
            return ResultJson.errorResultJson("处理反馈失败!");
        }
    }

    /**
     * 阅读,处理反馈
     * 3已读 4已处理
     */
    @RequestMapping(value = "/doUpdate.json", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject doUpdate(Integer id,Integer status) {
        LoginUser loginUser = LoginUser.getUser();
        Integer result = questionsFeedbackService.doUpdate(id, loginUser.getUserID(),status);
        if (result > 0) {
            return ResultJson.succResultJson("阅读or处理成功");
        } else {
            return ResultJson.errorResultJson("修改反馈失败!");
        }
    }


    /**
     * 获取反馈列表
     */
    @RequestMapping(value = "/getQuestionFeedbackList.json", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getQuestionFeedbackList(Integer questionId, PageEntity pageEntity) {
        JSONObject questionFeedBackJson = questionsFeedbackService.findFeedbackList(questionId, pageEntity);
        return questionFeedBackJson;
    }



}
