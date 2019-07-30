package com.enterprise.mobile.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.BaseController;
import com.enterprise.base.common.MobileLoginUser;
import com.enterprise.base.common.ResultJson;
import com.enterprise.base.entity.QuestionsFeedbackEntity;
import com.enterprise.base.enums.StatusEnum;
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
    @RequestMapping(value = "/createOrUpdate.json", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject createOrUpdateQuestionLibrary(QuestionsFeedbackEntity questionsFeedbackEntity) {
        MobileLoginUser loginUser = MobileLoginUser.getUser();
        questionsFeedbackEntity.setUserId(loginUser.getUserID());
        questionsFeedbackEntity.setStatus(StatusEnum.OK.getValue());
        Integer result = questionsFeedbackService.createOrUpdateQuestionFeedback(questionsFeedbackEntity);
        if (result > 0) {
            return ResultJson.succResultJson(questionsFeedbackEntity);
        } else {
            return ResultJson.errorResultJson("反馈题目失败!");
        }
    }

}
