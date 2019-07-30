package com.enterprise.mobile.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.BaseController;
import com.enterprise.base.common.MobileLoginUser;
import com.enterprise.base.common.ResultJson;
import com.enterprise.base.enums.QuestionAnswerSourceEnum;
import com.enterprise.base.vo.dto.QuestionAnswerDto;
import com.enterprise.service.bagTool.BagToolService;
import com.enterprise.service.question.QuestionsService;
import com.enterprise.service.question.UserXQuestionsService;
import com.enterprise.util.AssertUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 答题
 * Created by anson on 18/4/2.
 */
@Controller
@RequestMapping("/answer")
public class AnswerController extends BaseController {

    @Resource
    private UserXQuestionsService userXQuestionsService;

    @Resource
    private QuestionsService questionsService;

    @Resource
    private BagToolService bagToolService;

    /**
     * 获取用户默认的题目
     */
    @RequestMapping(value = "/getNewBieQuestion.json", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getNewBieQuestion() {
        MobileLoginUser mobileLoginUser = MobileLoginUser.getUser();
        return questionsService.getNewBieQuestion(mobileLoginUser);
    }

    /**
     * 新手引导-用户答题
     *
     * @Param questionAnswerDto 回答时的参数
     * @Param effedcId 被作用的影响id
     */
    @RequestMapping(value = "/answer.json", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject answer(QuestionAnswerDto questionAnswerDto, Integer effedcId) {
        try {
            AssertUtil.notNull(questionAnswerDto.getQuestionId(), "题目id不能为空");
            AssertUtil.isTrue(questionAnswerDto.getAnswer().length() > 0 , "请输入合法的答案!");
            questionAnswerDto.setType(QuestionAnswerSourceEnum.AUTONOMOUS_ANSWER.getValue());
            return userXQuestionsService.answer(questionAnswerDto, effedcId);
        } catch (Exception e) {
            return ResultJson.errorResultJson(e.getMessage());
        }
    }

    /**
     * 新手引导-用户答题
     *
     * @Param questionAnswerDto 回答时的参数
     * @Param effedcId 被作用的影响id
     */
    @RequestMapping(value = "/reviewAnswer.json", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject reviewAnswer(QuestionAnswerDto questionAnswerDto) {
        try {
            AssertUtil.notNull(questionAnswerDto.getQuestionId(), "题目id不能为空");
            AssertUtil.isTrue(questionAnswerDto.getAnswer().length() > 0 , "请输入合法的答案!");
            questionAnswerDto.setType(QuestionAnswerSourceEnum.AUTONOMOUS_ANSWER.getValue());
            return userXQuestionsService.reviewAnswer(questionAnswerDto);
        } catch (Exception e) {
            return ResultJson.errorResultJson(e.getMessage());
        }
    }


    /**
     * 当前道具还需要答题多少
     */
    @RequestMapping(value = "/getToolAnswerCount.json", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getToolAnswerCount(Integer toolEffectId) {
        return bagToolService.getToolAnswerCount(toolEffectId);
    }

}
