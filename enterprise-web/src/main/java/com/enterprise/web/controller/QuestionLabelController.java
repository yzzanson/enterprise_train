package com.enterprise.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.LoginUser;
import com.enterprise.base.common.PageEntity;
import com.enterprise.base.common.ResultJson;
import com.enterprise.base.entity.QuestionLabelEntity;
import com.enterprise.service.question.QuestionLabelService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/6/6 下午2:21
 */
@Controller
@RequestMapping("/question_label")
public class QuestionLabelController {

    @Resource
    private QuestionLabelService questionLabelService;

    /**
     * 新增or修改标签库
     * */
    @RequestMapping(value="/createOrUpdate.json",method= RequestMethod.POST)
    @ResponseBody
    public JSONObject createOrUpdateQuestionLibrary(QuestionLabelEntity questionLabelEntity){
        LoginUser loginUser = LoginUser.getUser();
        questionLabelEntity.setCompanyId(loginUser.getCompanyID());
        questionLabelEntity.setUserId(loginUser.getUserID());
        Integer result =questionLabelService.createOrUpdateQuestionLabel(questionLabelEntity);
        if (result > 0) {
            return ResultJson.succResultJson(questionLabelEntity);
        } else {
            return ResultJson.errorResultJson("新增or修改题目标签失败!");
        }
    }


    /**
     * 新增or修改标签库
     * */
    @RequestMapping(value="/batchSafeDelete.json",method= RequestMethod.POST)
    @ResponseBody
    public JSONObject batchSafeDelete(String labelIds){
        Integer result =questionLabelService.batchSafeDelete(labelIds);
        if (result > 0) {
            return ResultJson.succResultJson(result);
        } else {
            return ResultJson.errorResultJson("新增or修改题目标签失败!");
        }
    }


    /**
     * 获取题库下标签列表
     */
    @RequestMapping(value = "/getQuestionLabels.json", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getQuestionLabelsByLibraryId(Integer libraryId,String name,PageEntity pageEntity) {
        JSONObject questionDetailJson = questionLabelService.getQuestionLabelsByLibraryId(libraryId, name, pageEntity);
        return questionDetailJson;
    }


    /**
     * 根据id获取标签详情
     */
    @RequestMapping(value = "/getQuestionLabelById.json", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getQuestionLabelById(Integer id) {
        JSONObject questionDetailJson = questionLabelService.getQuestionLabelById(id);
        return questionDetailJson;
    }





}
