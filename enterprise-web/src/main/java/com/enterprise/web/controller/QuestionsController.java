package com.enterprise.web.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.BaseController;
import com.enterprise.base.common.LoginUser;
import com.enterprise.base.common.PageEntity;
import com.enterprise.base.common.ResultJson;
import com.enterprise.base.dto.QuestionsDTO;
import com.enterprise.base.entity.QuestionsEntity;
import com.enterprise.base.enums.QuestionTypeEnum;
import com.enterprise.base.enums.StatusEnum;
import com.enterprise.service.question.QuestionsService;
import com.enterprise.util.AssertUtil;
import com.enterprise.util.DateUtil;
import com.enterprise.util.QuestionUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Map;

/**
 * Created by anson on 18/3/27.
 */
@Controller
@RequestMapping("/questions")
public class QuestionsController extends BaseController {

    @Resource
    private QuestionsService questionsService;

    /**
     * 新增or修改题库
     * 多选题答案用,分隔
     */
    @RequestMapping(value = "/createOrUpdate.json", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject createOrUpdateQuestionLibrary(QuestionsDTO questionDto) {
        LoginUser loginUser = LoginUser.getUser();
        QuestionsEntity questionsEntity = new QuestionsEntity();
        try {
            if (questionDto.getType().equals(QuestionTypeEnum.SINGLE_CHOICE.getType()) || questionDto.getType().equals(QuestionTypeEnum.MULTI_CHOICE.getType())) {

                if(questionDto.getType().equals(QuestionTypeEnum.MULTI_CHOICE.getType()) && StringUtils.isNotEmpty(questionDto.getAnswer())){
                    Boolean rightAnswer = QuestionUtil.answerMatchInsert(questionDto.getAnswer());
                    if(!rightAnswer){
                        return ResultJson.errorResultJson("不正确的答案格式");
                    }else{
                        questionDto.setAnswer(QuestionUtil.answerChange(questionDto.getAnswer()));
                    }
                }

                BeanUtils.copyProperties(questionsEntity, questionDto);
                if (questionDto.getLabelId() == null) {
                    questionsEntity.setLabelId(null);
                }

                if (questionDto.getId() != null) {
                    if (StringUtils.isNotEmpty(questionDto.getDescription())) {
                        AssertUtil.notNull(questionDto.getDescription(), "标题不能为空!");
                    }
                    if (StringUtils.isNotEmpty(questionDto.getAnswer())) {
                        AssertUtil.notNull(questionDto.getAnswer(), "答案不能为空!");
                    }
                    if (StringUtils.isNotEmpty(questionDto.getDescription())) {
                        AssertUtil.isTrue(questionDto.getDescription().length() <= 80, "题目长度超过最大限制!");
                    }
                    QuestionsEntity questionsEntityInDB = questionsService.getById(questionDto.getId());
                    JSONObject optionsJson = JSONObject.parseObject(questionsEntityInDB.getOptions());
                    if (StringUtils.isNotEmpty(questionDto.getOptionA())) {
                        AssertUtil.isTrue(questionDto.getOptionA().length() <= 40, "答案长度超过最大限制!");
                        optionsJson.put("A", questionDto.getOptionA());
                    }
                    if (StringUtils.isNotEmpty(questionDto.getOptionB())) {
                        AssertUtil.isTrue(questionDto.getOptionB().length() <= 40, "答案长度超过最大限制!");
                        optionsJson.put("B", questionDto.getOptionB());
                    }
                    if (StringUtils.isNotEmpty(questionDto.getOptionC())) {
                        AssertUtil.isTrue(questionDto.getOptionC().length() <= 40, "答案长度超过最大限制!");
                        optionsJson.put("C", questionDto.getOptionC());
                    }
                    if (StringUtils.isNotEmpty(questionDto.getOptionD())) {
                        AssertUtil.isTrue(questionDto.getOptionD().length() <= 40, "答案长度超过最大限制!");
                        optionsJson.put("D", questionDto.getOptionD());
                    }
                    if (StringUtils.isNotEmpty(questionDto.getOptionA()) || StringUtils.isNotEmpty(questionDto.getOptionB()) || StringUtils.isNotEmpty(questionDto.getOptionC()) || StringUtils.isNotEmpty(questionDto.getOptionD())) {
                        questionsEntity.setOptions(optionsJson.toString());
                    }
                    questionsEntity.setUpdateTime(new Date());
                } else {
                    AssertUtil.notNull(questionDto.getDescription(), "标题不能为空!");
                    AssertUtil.notNull(questionDto.getAnswer(), "答案不能为空!");
                    AssertUtil.isTrue(questionDto.getDescription().length() <= 80, "题目长度超过最大限制!");
                    questionsEntity.setOperator(loginUser.getUserID());
                    String optionsString = parseChoiceToJson(questionDto);
                    questionsEntity.setOptions(optionsString);
                }
            //判断题 A正确 B错误
            }else if(questionDto.getType().equals(QuestionTypeEnum.TRUE_FALSE.getType())){
                BeanUtils.copyProperties(questionsEntity, questionDto);
                questionsEntity.setOptions(QuestionUtil.parseRighrtWrongChoiceToJson());
                questionsEntity.setOperator(loginUser.getUserID());
                if (questionDto.getId() != null) {
                    AssertUtil.notNull(questionDto.getDescription(), "标题不能为空!");
                    AssertUtil.notNull(questionDto.getAnswer(), "答案不能为空!");
                }
            //填空题
            }else{
                BeanUtils.copyProperties(questionsEntity, questionDto);
                Map<String,Object> descriptionParseMap = QuestionUtil.parseDescription(questionDto.getDescription());
                String description = (String) descriptionParseMap.get("description");
                String answer = (String) descriptionParseMap.get("answer");
                JSONArray answerJsonArray = QuestionUtil.parseBlankTOJsonArray(answer);
                questionsEntity.setDescription(description);
                questionsEntity.setOperator(loginUser.getUserID());
                questionsEntity.setAnswer(answerJsonArray.toJSONString());
            }
            if (questionDto.getRestudy() == null) {
                questionsEntity.setRestudy(StatusEnum.DELETE.getValue());
            }

            Integer result = questionsService.createOrUpdateQuestion(questionsEntity);
            //更新学习进度,题库题目总数
            if (result > 0) {
//                if (questionDto.getId() == null || (questionDto.getRestudy() != null && questionDto.getRestudy() == 1)) {
//                    if(questionDto.getRestudy() != null && questionDto.getRestudy() == 1) {
//                        new Thread(new ModifyUserQuestionThread(loginUser.getCompanyID(),questionDto.getId(),0)).start();
//                    }else{
//                        new Thread(new ModifyUserQuestionThread(loginUser.getCompanyID(),questionDto.getId(),1)).start();
//                    }
//                }
                return ResultJson.succResultJson(questionsEntity);
            } else {
                return ResultJson.errorResultJson("新增or修改题失败!");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 点击编辑-获取问题列表
     */
    @RequestMapping(value = "/getQuestionList.json", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getQuestionList(Integer libraryId, String name, String label, PageEntity pageEntity) {
        JSONObject questionListJson = questionsService.getQuestionList(libraryId, name, label, pageEntity);
        return questionListJson;
    }


    /**
     * 点击编辑-获取题目详情
     */
    @RequestMapping(value = "/getQuestionDetailById.json", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getQuestionDetail(Integer id) {
        JSONObject questionDetailJson = questionsService.getQuestionDetailById(id);
        return questionDetailJson;
    }


    /**
     * 新增or修改题库
     */
    @RequestMapping(value = "/delete.json", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject delete(String ids) {
        return questionsService.batchDelete(ids);
    }


    /**
     * 新增or修改题库
     */
    @RequestMapping(value = "/reestudyBatch.json", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject reestudyBatch(String ids) {
        return questionsService.batchRestudyQuestion(ids);
    }



    private static String parseChoiceToJson(QuestionsDTO QuestionDto) {
        JSONObject jsonObject = new JSONObject();
        if (StringUtils.isNotEmpty(QuestionDto.getOptionA())) {
            AssertUtil.isTrue(QuestionDto.getOptionA().length() <= 40, "答案长度超过最大限制!");
            jsonObject.put("A", QuestionDto.getOptionA());
        } else jsonObject.put("A", "");
        if (StringUtils.isNotEmpty(QuestionDto.getOptionB())) {
            AssertUtil.isTrue(QuestionDto.getOptionB().length() <= 40, "答案长度超过最大限制!");
            jsonObject.put("B", QuestionDto.getOptionB());
        } else jsonObject.put("B", "");
        if (StringUtils.isNotEmpty(QuestionDto.getOptionC())) {
            AssertUtil.isTrue(QuestionDto.getOptionC().length() <= 40, "答案长度超过最大限制!");
            jsonObject.put("C", QuestionDto.getOptionC());
        } else jsonObject.put("C", "");
        if (StringUtils.isNotEmpty(QuestionDto.getOptionD())) {
            AssertUtil.isTrue(QuestionDto.getOptionD().length() <= 40, "答案长度超过最大限制!");
            jsonObject.put("D", QuestionDto.getOptionD());
        } else jsonObject.put("D", "");
//        return jsonObject.toString();
        return jsonObject.toJSONString();
    }


    public static void main(String[] args) {


//        QuestionsDTO questionDto = new QuestionsDTO();
//        questionDto.setOptionA("aa");
//        questionDto.setOptionB("bb");
//        questionDto.setOptionC("cc");
//        questionDto.setOptionD("dd");
//        questionDto.setDescription("中国金融期货交易所在上海挂牌成立的时劳动解");
//        questionDto.setAnswer("sss");
//        questionDto.setRestudy(1);
//        questionDto.setAnswerDesc("saaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaass");
//        System.out.println(questionDto.getAnswerDesc().length());
//        AssertUtil.notNull(questionDto.getDescription(), "标题不能为空!");
//        AssertUtil.notNull(questionDto.getAnswer(), "答案不能为空!");
//        if(StringUtils.isNotEmpty(questionDto.getAnswerDesc())){
//            AssertUtil.isTrue( questionDto.getAnswerDesc().length() <= 80, "解析长度超过最大限制!");
//        }
//        if(questionDto.getRestudy().equals(1)){
//            System.out.println(11111);
//        }
//
//        if(questionDto.getRestudy()==1){
//            System.out.println(22222);
//        }

//        String str = "b,a,c";
//        String result = QuestionUtil.answerChange(str);
//        System.out.println(result);

        Date date = new Date(1559805152085l);
        System.out.println(DateUtil.getDisplayYMDHMS(date));

//        System.out.println(questionDto.getDescription().length());
//        AssertUtil.isTrue(questionDto.getDescription().length() <= 20, "题目长度超过最大限制!");

    }

}
