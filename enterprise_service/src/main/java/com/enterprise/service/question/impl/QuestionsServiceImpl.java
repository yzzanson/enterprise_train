package com.enterprise.service.question.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.*;
import com.enterprise.base.entity.*;
import com.enterprise.base.enums.BagToolTypeEnum;
import com.enterprise.base.enums.QuestionTypeEnum;
import com.enterprise.base.enums.ResultCodeEnum;
import com.enterprise.base.enums.StatusEnum;
import com.enterprise.base.exceptions.BusinessException;
import com.enterprise.base.vo.*;
import com.enterprise.cache.GetCache;
import com.enterprise.mapper.bagTool.BagToolEffectMapper;
import com.enterprise.mapper.companyInfo.CompanyInfoMapper;
import com.enterprise.mapper.questions.QuestionsFeedbackMapper;
import com.enterprise.mapper.questions.QuestionsLibraryMapper;
import com.enterprise.mapper.questions.QuestionsMapper;
import com.enterprise.mapper.questions.UserXQuestionsMapper;
import com.enterprise.mapper.users.UserConfigMapper;
import com.enterprise.oa.OAMessageUtil;
import com.enterprise.service.question.QuestionLabelService;
import com.enterprise.service.question.QuestionLibraryTitleService;
import com.enterprise.service.question.QuestionsLibraryService;
import com.enterprise.service.question.QuestionsService;
import com.enterprise.service.redis.RedisService;
import com.enterprise.thread.ModifyRestudyThread;
import com.enterprise.thread.ModifyUserQuestionLibraryThread;
import com.enterprise.thread.ModifyUserQuestionThread;
import com.enterprise.util.QuestionUtil;
import com.enterprise.util.excel.ExcelQuestionUtil;
import com.enterprise.util.excel.QuestionExcelDTO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by anson on 18/3/27.
 */
@Service
public class QuestionsServiceImpl implements QuestionsService {

    private static final String BLANK_QUESTION_TYPE_CHINESE = QuestionTypeEnum.COMPLETION.getChineseDesc();

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private QuestionsMapper questionsMapper;

    @Resource
    private QuestionsLibraryMapper questionsLibraryMapper;

    @Resource
    private QuestionsLibraryService questionsLibraryService;

    @Resource
    private CompanyInfoMapper companyInfoMapper;

    @Resource
    private UserXQuestionsMapper userXQuestionsMapper;

    @Resource
    private QuestionsFeedbackMapper questionsFeedbackMapper;

    @Resource
    private QuestionLabelService questionLabelService;

    @Resource
    private QuestionLibraryTitleService questionLibraryTitleService;

    @Resource
    private BagToolEffectMapper bagToolEffectMapper;

    @Resource
    private UserConfigMapper userConfigMapper;

    @Resource
    private QuestionsService questionsService;

    public static String Question_KEY = RedisConstant.QUESTION_KEY;

    @Resource
    private RedisService redisService;

    private Long expireTime = RedisConstant.EXPIRE_TIME_COMMON;

    private static String EXAMPLE_QUESTION_WORD = "【样例】";

    @Override
    public Integer createOrUpdateQuestion(QuestionsEntity questionsEntity) {
        if (questionsEntity.getId() == null || (questionsEntity.getId() != null && questionsEntity.getId().equals(0))) {
            Integer result = questionsMapper.createOrUpdateQuestion(questionsEntity);
            if (questionsEntity.getLibraryId() != null && questionsEntity.getLibraryId() > 0) {
                Integer questoinCount = questionsMapper.getTotalCountByLibraryId(questionsEntity.getLibraryId());
                QuestionsLibraryEntity questionsLibraryEntity = new QuestionsLibraryEntity(questionsEntity.getLibraryId(), questoinCount, new Date());
                questionsLibraryMapper.updateQuestionLibrary(questionsLibraryEntity);
            }
            String key = Question_KEY + "id_" + questionsEntity.getId();
            redisService.setSerialize(key, expireTime, questionsEntity);
            return result;
        } else {
            String key = Question_KEY + "id_" + questionsEntity.getId();
            if (questionsEntity.getLibraryId() != null && questionsEntity.getLibraryId() > 0) {
                questionsLibraryMapper.updateQuestionLibrary(new QuestionsLibraryEntity(questionsEntity.getLibraryId(), new Date()));
            } else {
                QuestionsEntity questionsEntity1 = questionsService.getById(questionsEntity.getId());
                if (questionsEntity1.getLibraryId() != null && questionsEntity1.getLibraryId() > 0) {
                    questionsLibraryMapper.updateQuestionLibrary(new QuestionsLibraryEntity(questionsEntity1.getLibraryId(), new Date()));
                }
            }
            Integer result = questionsMapper.updateQuestion(questionsEntity);
            redisService.setSerialize(key, expireTime, questionsEntity);
            return result;
        }
    }


    /**
     * 重新学习
     */
    @Override
    public JSONObject batchRestudyQuestion(String ids) {
        if (StringUtils.isNotEmpty(ids)) {
            String[] idArray = ids.split(",");
            List<Integer> questionList = new ArrayList<>();
            for (int i = 0; i < idArray.length; i++) {
                Integer questionId = Integer.valueOf(idArray[i].trim());
                QuestionsEntity questionEntity = questionsMapper.getById(questionId);
                if (questionEntity != null) {
                    questionList.add(questionEntity.getId());
                }
            }
            QuestionsEntity questionEntity = questionsMapper.getById(questionList.get(0));
            Integer libraryId = questionEntity.getLibraryId();
            //获取回答过该题的公司,用户列表 type=0 answerstatus=1 status=1
            List<UserXQuestionsEntity> answerUserCompanyList = userXQuestionsMapper.getIsAnswerUser(questionList);
            //对这些用户的回答记录进行更新
            for (int i = 0; i < questionList.size(); i++) {
                userXQuestionsMapper.batchUpdateStatus(questionList.get(i), StatusEnum.HOLD_ON.getValue());
            }
            //重算该些用户的回答进度
            new Thread(new ModifyRestudyThread(libraryId, answerUserCompanyList)).start();
            return ResultJson.succResultJson("成功");
        }
        return ResultJson.errorResultJson("不存在的题号");
    }

    /**
     * 新的列表
     */
    @Override
    public JSONObject getQuestionList(Integer libraryId, String name, String label, PageEntity pageEntity) {
        List<String> labelList = new ArrayList<>();
        if (StringUtils.isNotEmpty(label)) {
            String[] labelArr = label.split(",");
            for (int i = 0; i < labelArr.length; i++) {
                labelList.add(labelArr[i]);
            }
        }
        PageHelper.startPage(pageEntity.getPageNo(), pageEntity.getPageSize());
        PageInfo<QuestionVO> pageInfo = new PageInfo<>(questionsMapper.getQuestionList(libraryId, name, labelList));
        Map<Integer, String> questionLabelMap = questionLabelService.getLabelCollectionByLibraryId(libraryId);
        List<QuestionVO> questionsListInDB = new ArrayList<>();
        if (pageInfo.getList() != null && pageInfo.getList().size() > 0) {
            for (int i = 0; i < pageInfo.getList().size(); i++) {
                QuestionVO questionVO = pageInfo.getList().get(i);
                String questionType = questionVO.getType();
                Integer questionId = questionVO.getId();
                //设置问题有多少反馈
                Integer feedBackCount = questionsFeedbackMapper.getFeedbackCount(questionId);
                questionVO.setFeedbackCount(feedBackCount);
                if (questionLabelMap != null && questionLabelMap.size() > 0) {
                    questionVO.setLabelName(questionLabelMap.get(questionVO.getLabelId()));
                }
                //除了单选题都要解析
                if (StringUtils.isNotEmpty(questionType) && !questionType.equals(BLANK_QUESTION_TYPE_CHINESE)) {
                    parseOptionFromDB(questionVO);
                }
                questionsListInDB.add(questionVO);
            }
        }
        Map<String, Object> dataMap = Maps.newHashMap();
        dataMap.put("list", questionsListInDB);
        dataMap.put("total", pageInfo.getTotal());
        dataMap.put("page", new Page(pageInfo.getPrePage(), pageEntity.getPageNo(), pageInfo.getNextPage(), pageInfo.getLastPage()));
        return ResultJson.succResultJson(dataMap);
    }


    private void parseOptionFromDB(QuestionVO questionVO) {
        String questionType = questionVO.getType();
        JSONObject optionsJson = JSONObject.parseObject(questionVO.getOptions());
        questionVO.setOptionA(optionsJson.getString("A"));
        questionVO.setOptionB(optionsJson.getString("B"));
        if (questionType.equals(QuestionTypeEnum.SINGLE_CHOICE.getChineseDesc()) || (questionType.equals(QuestionTypeEnum.MULTI_CHOICE.getChineseDesc()) && StringUtils.isNotEmpty(optionsJson.getString("C")))) {
            questionVO.setOptionC(optionsJson.getString("C"));
        }
        if (questionType.equals(QuestionTypeEnum.SINGLE_CHOICE.getChineseDesc()) || (questionType.equals(QuestionTypeEnum.MULTI_CHOICE.getChineseDesc()) && StringUtils.isNotEmpty(optionsJson.getString("D")))) {
            questionVO.setOptionD(optionsJson.getString("D"));
        }
    }

    @Override
    public JSONObject batchDelete(String ids) {
        try {
            String[] idArray = ids.split(",");
            List<Integer> idList = new ArrayList<>();
            for (int i = 0; i < idArray.length; i++) {
                if (StringUtils.isNotEmpty(idArray[i])) {
                    idList.add(Integer.valueOf(idArray[i].trim()));
                }
            }
            Integer result = questionsMapper.batchDelete(idList);
            if (result > 0) {
                QuestionsEntity questionsEntity1 = questionsService.getById(idList.get(0));
                Integer libraryId = questionsEntity1.getLibraryId();
                Integer totalCount = questionsMapper.getTotalCountByLibraryId(libraryId);
                if (questionsEntity1.getLibraryId() != null && questionsEntity1.getLibraryId() > 0) {
                    questionsLibraryMapper.updateQuestionLibrary(new QuestionsLibraryEntity(questionsEntity1.getLibraryId(), totalCount, new Date()));
                }
                new Thread(new ModifyUserQuestionThread(ids, 2)).start();
                //更新题库进度
                return ResultJson.succResultJson(ids);
            }
            return ResultJson.errorResultJson("更新失败");
        } catch (Exception e) {
            return ResultJson.errorResultJson("更新失败:" + e.getMessage());
        }
    }

    @Override
    @GetCache(name = "question", value = "id")
    public QuestionsEntity getById(Integer id) {
        return questionsMapper.getById(id);
    }

    @Override
    public JSONObject getQuestionDetailList(Integer libraryId, PageEntity pageEntity) {
        LoginUser loginUser = LoginUser.getUser();
        PageHelper.startPage(pageEntity.getPageNo(), pageEntity.getPageSize());
        PageInfo<QuestionVO1> pageInfo = new PageInfo<>(questionsMapper.getQuestionDetailList(libraryId));
        List<QuestionDetailVO> questionDetailVOList = new ArrayList<>();
        for (int i = 0; i < pageInfo.getList().size(); i++) {
            QuestionVO1 questionsEntity = pageInfo.getList().get(i);
            QuestionDetailVO questionDetailVO = new QuestionDetailVO();
            try {
                BeanUtils.copyProperties(questionDetailVO, questionsEntity);
                JSONObject optionsJson = JSONObject.parseObject(questionsEntity.getOptions());
                String optionA = optionsJson.getString("A");
                String optionB = optionsJson.getString("B");
                questionDetailVO.setOptionA(optionA);
                questionDetailVO.setOptionB(optionB);
                if (StringUtils.isNotEmpty(optionsJson.getString("C"))) {
                    String optionC = optionsJson.getString("C");
                    questionDetailVO.setOptionC(optionC);
                }
                if (StringUtils.isNotEmpty(optionsJson.getString("D"))) {
                    String optionD = optionsJson.getString("D");
                    questionDetailVO.setOptionD(optionD);
                }
                questionDetailVO.setLabel(questionsEntity.getLabelName());
                questionDetailVOList.add(questionDetailVO);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        QuestionsLibraryEntity questionsLibraryEntity = questionsLibraryService.getById(libraryId);
        QuestionLibrarySimpleVO questionLibrarySimpleVO = new QuestionLibrarySimpleVO();
        questionLibrarySimpleVO.setId(libraryId);
        questionLibrarySimpleVO.setName(questionsLibraryEntity.getName());
        Map<String, Object> dataMap = Maps.newHashMap();
        dataMap.put("library", questionLibrarySimpleVO);
        dataMap.put("list", questionDetailVOList);
        dataMap.put("total", pageInfo.getTotal());
        dataMap.put("page", new Page(pageInfo.getPrePage(), pageEntity.getPageNo(), pageInfo.getNextPage(), pageInfo.getLastPage()));
        return ResultJson.succResultJson(dataMap);
    }

    @Override
    public JSONObject getNewBieQuestion(MobileLoginUser mobileLoginUser) {
        QuestionsEntity questionsEntity = questionsMapper.getNewBieQuestion(mobileLoginUser.getCompanyID());
        if (questionsEntity == null) {
            questionsEntity = questionsMapper.getNewBieQuestion2(mobileLoginUser.getCompanyID());
        }

        QuestionDetailVO questionDetailVO = new QuestionDetailVO();
        try {
            if (questionsEntity != null) {
                BeanUtils.copyProperties(questionDetailVO, questionsEntity);
                JSONObject optionsJson = JSONObject.parseObject(questionsEntity.getOptions());
                String optionA = optionsJson.getString("A");
                String optionB = optionsJson.getString("B");
                questionDetailVO.setOptionA(optionA);
                questionDetailVO.setOptionB(optionB);
                if (StringUtils.isNotEmpty(optionsJson.getString("C"))) {
                    String optionC = optionsJson.getString("C");
                    questionDetailVO.setOptionC(optionC);
                }
                if (StringUtils.isNotEmpty(optionsJson.getString(""))) {
                    String optionD = optionsJson.getString("D");
                    questionDetailVO.setOptionD(optionD);
                }
                return ResultJson.succResultJson(questionDetailVO);
            } else {
                return ResultJson.errorResultJson("没有可以使用的题目");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * type 0/1 随机/顺序
     */
    @Override
    public JSONObject getNextQuestion(Integer libraryId, Integer companyId, Integer userId) throws BusinessException {
        QuestionsLibraryEntity questionsLibraryEntity = questionsLibraryService.getById(libraryId);
        Integer sortType = questionsLibraryEntity.getSortType() == null ? 1 : questionsLibraryEntity.getSortType();
        Integer optionSortType = questionsLibraryEntity.getOptionSortType() ==null ? 1:questionsLibraryEntity.getOptionSortType();
        UserXTitleSimpleVO userXTitleSimpleVO = null;
        QuestionLibraryTitleEntity questionLibraryTitleEntity = questionLibraryTitleService.getQuestionTitleByLibrary(libraryId);
        if (questionLibraryTitleEntity != null) {
            userXTitleSimpleVO = new UserXTitleSimpleVO(questionLibraryTitleEntity.getTitle(), questionLibraryTitleEntity.getType(), MobileLoginUser.getUser().getAvatar());
        }
        Integer correctAnsweredQuestionNumber = userXQuestionsMapper.getUserCorrectQuestionsByLibraryId(companyId, userId, libraryId);//getUserCorrectCount(userXQuestionsEntity.getUserId(), questionsEntity.getLibraryId());
        Integer totalQuestionCount = questionsMapper.getTotalCountByLibraryId(libraryId);//题库下所有的题目
//        Integer totalUserCorrectCount = userXQuestionMapper.getTotalCorrectCountByLibraryId(libraryId, userId);//该用户所有答对的题目数
        if (correctAnsweredQuestionNumber >= totalQuestionCount) {
            //题库学习完毕
            return ResultJson.finishResultJson(ResultCodeEnum.KICKED_OUT.getValue(), "该题库已学习完毕", userXTitleSimpleVO);
        }
        //获取当前所有未答对的题
        List<Integer> wrongAnswerQuestions = userXQuestionsMapper.getwrongAnsweredQuestions(companyId, libraryId, userId);
        Integer questionId = userXQuestionsMapper.getNewQuesion(companyId, libraryId, userId, sortType);

        if (questionId == null) {
            if (wrongAnswerQuestions.size() == 0) {
                //题库学习完毕
                return ResultJson.finishResultJson(ResultCodeEnum.KICKED_OUT.getValue(), "该题库已学习完毕", userXTitleSimpleVO);
            } else {
                Integer index = new Random().nextInt(wrongAnswerQuestions.size());
                questionId = wrongAnswerQuestions.get(index);
            }
        }
        //是否有作用在该用户身上的小衰神
        List<BagToolEffectEntity> bagToolList = bagToolEffectMapper.getUserEffectedStatusOther(companyId, userId, BagToolTypeEnum.DECAY.getValue());

        QuestionDetailVO questionDetailVO = getQuestionDetail(questionId);
        questionDetailVO.setAnswerDesc(null);
        questionDetailVO.setTotalCount(totalQuestionCount);
        questionDetailVO.setAnsweredCount(correctAnsweredQuestionNumber);
//        questionDetailVO.setSortType(optionSortType);
        if (questionDetailVO != null) {
            QuestionDetailNextVO questionDetailNextVO = new QuestionDetailNextVO();
            try {
                BeanUtils.copyProperties(questionDetailNextVO, questionDetailVO);
                if (bagToolList != null && bagToolList.size() > 0) {
                    questionDetailNextVO.setIsDecay(1);
                } else {
                    questionDetailNextVO.setIsDecay(0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            questionDetailNextVO.setSortType(optionSortType);
            return ResultJson.succResultJson(questionDetailNextVO);
        }

        return ResultJson.errorResultJson("不存在的questionId");
    }


    /**
     * SimpleCORSFilter
     * type 0/1 随机/顺序
     */
    @Override
    public JSONObject getNextQuestionTest(Integer libraryId, Integer companyId, Integer userId) {
        QuestionsLibraryEntity questionsLibraryEntity = questionsLibraryService.getById(libraryId);
        if (questionsLibraryEntity == null) {
            return ResultJson.errorResultJson("不存在的题库");
        }
        Integer sortType = questionsLibraryEntity.getSortType() == null ? 1 : questionsLibraryEntity.getSortType();
        UserXTitleSimpleVO userXTitleSimpleVO = null;
//        QuestionLibraryTitleEntity questionLibraryTitleEntity = questionLibraryTitleMapper.getQuestionTitleByLibrary(libraryId);
        QuestionLibraryTitleEntity questionLibraryTitleEntity = questionLibraryTitleService.getQuestionTitleByLibrary(libraryId);
        if (questionLibraryTitleEntity != null) {
            userXTitleSimpleVO = new UserXTitleSimpleVO(questionLibraryTitleEntity.getTitle(), questionLibraryTitleEntity.getType(), null);
        }
        Integer correctAnsweredQuestionNumber = userXQuestionsMapper.getUserCorrectQuestionsByLibraryId(companyId, userId, libraryId);//getUserCorrectCount(userXQuestionsEntity.getUserId(), questionsEntity.getLibraryId());
        Integer totalQuestionCount = questionsMapper.getTotalCountByLibraryId(libraryId);//题库下所有的题目
//        Integer totalUserCorrectCount = userXQuestionMapper.getTotalCorrectCountByLibraryId(libraryId, userId);//该用户所有答对的题目数
        if (correctAnsweredQuestionNumber >= totalQuestionCount) {
            //题库学习完毕
            return ResultJson.finishResultJson(ResultCodeEnum.KICKED_OUT.getValue(), "该题库已学习完毕", userXTitleSimpleVO);
        }
        //获取当前所有未答对的题

        Integer questionId = userXQuestionsMapper.getNewQuesion(companyId, libraryId, userId, sortType);
        if (questionId == null) {
            List<Integer> wrongAnswerQuestions = userXQuestionsMapper.getwrongAnsweredQuestions(companyId, libraryId, userId);
            if (wrongAnswerQuestions.size() == 0) {
                //题库学习完毕
                return ResultJson.finishResultJson(ResultCodeEnum.KICKED_OUT.getValue(), "该题库已学习完毕", userXTitleSimpleVO);
            } else {
                Integer index = new Random().nextInt(wrongAnswerQuestions.size());
                questionId = wrongAnswerQuestions.get(index);
            }
        }
        //是否有作用在该用户身上的小衰神
        QuestionDetailVO questionDetailVO = getQuestionDetail(questionId);
        questionDetailVO.setAnswerDesc(null);
        questionDetailVO.setTotalCount(totalQuestionCount);
        questionDetailVO.setAnsweredCount(correctAnsweredQuestionNumber);
        if (questionDetailVO != null) {
            QuestionDetailNextVO questionDetailNextVO = new QuestionDetailNextVO();
            try {
                BeanUtils.copyProperties(questionDetailNextVO, questionDetailVO);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ResultJson.succResultJson(questionDetailNextVO);
        }

        return ResultJson.errorResultJson("不存在的questionId");
    }


    @Override
    public JSONObject getQuestionDetailById(Integer id) {
        QuestionsEntity questionsEntity = questionsService.getById(id);
        QuestionDetailVO questionDetailVO = getQuestionDetail(id);
        questionDetailVO.setAnswer(questionsEntity.getAnswer());
        return ResultJson.succResultJson(questionDetailVO);
    }

    //    public JSONObject importQuestions(FileItem file, Integer libraryId, String temppath) {
    @Override
    public JSONObject importQuestions(File file, Integer libraryId) {
        LoginUser loginUser = LoginUser.getUser();
        String libraryName = questionsLibraryMapper.getLibraryNameById(libraryId);
        //获取文件的流
        JSONObject jsonObject = new JSONObject();
        int totalnum = 0;
        try {
            //将文件保存到服务器
            //读取excel文件并查询mysql做插入
            InputStream is = new FileInputStream(file);
            List<QuestionExcelDTO> resultList = new ArrayList<>();
            if (file.getName().endsWith("xlsx")) {
                resultList = ExcelQuestionUtil.importXSSF(file);
            } else {
                resultList = ExcelQuestionUtil.importHSSF(file);
            }
            if (resultList == null || (resultList != null && resultList.size() == 0)) {
                return ResultJson.errorResultJson("没有数据!");
            }
            //先将标签都插入库
            questionsLibraryService.batchInsertQuestionLabel(loginUser.getCompanyID(), libraryId, resultList);
            List<QuestionExcelDTO> addQuestionsList = new ArrayList<>();
            //不用管更新,直接将题目导入题库
            for (int i = 0; i < resultList.size(); i++) {
                QuestionExcelDTO questionExcelDTO = resultList.get(i);
                String description = questionExcelDTO.getDescription();
                if (questionExcelDTO == null || questionExcelDTO.getType() == null || StringUtils.isEmpty(description) || StringUtils.isEmpty(questionExcelDTO.getAnswer()) || (StringUtils.isNotEmpty(description) &&  description.startsWith(EXAMPLE_QUESTION_WORD))) {
//                    resultList.remove(i);
                    continue;
                } else {
                    if (questionExcelDTO.getType().equals(QuestionTypeEnum.SINGLE_CHOICE.getType())) {
                        if (questionExcelDTO.getOptionA() == null ) {
//                            resultList.remove(i);
                            continue;
                        }
                    } else if (questionExcelDTO.getType().equals(QuestionTypeEnum.MULTI_CHOICE.getType())) {
                        Boolean matchAnswer = QuestionUtil.answerMatch(questionExcelDTO.getAnswer());
                        if (!matchAnswer) {
//                            resultList.remove(i);
                            continue;
                        }
                        String answer = questionExcelDTO.getAnswer();
                        answer = answer.replaceAll("\\|", ",");
                        System.out.println(answer);
                        questionExcelDTO.setAnswer(answer);
                    } else if (questionExcelDTO.getType().equals(QuestionTypeEnum.TRUE_FALSE.getType())) {
                        if (!(questionExcelDTO.getAnswer().equals("A") || questionExcelDTO.getAnswer().equals("B"))) {
//                            resultList.remove(i);
                            continue;
                        }
                    } else if (questionExcelDTO.getType().equals(QuestionTypeEnum.COMPLETION.getType())) {
                        Integer blankCount = QuestionUtil.paserBlankCount(questionExcelDTO.getDescription());
                        JSONArray blankAnswerArray = QuestionUtil.parseBlankTOJsonArray(questionExcelDTO.getAnswer());
                        if (blankAnswerArray == null) {
//                            resultList.remove(i);
                            continue;
                        }
                        if(!blankCount.equals(blankAnswerArray.size())){
//                            resultList.remove(i);
                            continue;
                        }
                        questionExcelDTO.setAnswer(blankAnswerArray.toJSONString());
                    }
                    addQuestionsList.add(questionExcelDTO);
                }
            }
            if (addQuestionsList == null || (addQuestionsList != null && addQuestionsList.size() == 0)) {
                return ResultJson.errorResultJson("没有数据!");
            }
            Map<String, Integer> questionLabelMap = questionLabelService.getLabelCollectionByLibraryId2(libraryId);
            List<QuestionsEntity> questionsList = new ArrayList<>();
            for (int i = 0; i < addQuestionsList.size(); i++) {
                QuestionsEntity questionEntity = new QuestionsEntity();
                QuestionExcelDTO questionExceDTO = addQuestionsList.get(i);
                String optionsJson = parseChoiceToJson(questionExceDTO);
                BeanUtils.copyProperties(questionEntity, questionExceDTO);
                if (StringUtils.isNotEmpty(questionExceDTO.getLabel())) {
                    questionEntity.setLabelId(questionLabelMap.get(questionExceDTO.getLabel()));
                }
                if (questionExceDTO.getType().equals(QuestionTypeEnum.COMPLETION.getType())) {
                    questionEntity.setBlankIndex(1);
                }
                questionEntity.setStatus(StatusEnum.OK.getValue());
                questionEntity.setType(questionExceDTO.getType());
                questionEntity.setLibraryId(libraryId);
                questionEntity.setOperator(loginUser.getUserID());
                questionEntity.setOptions(optionsJson);
                questionsList.add(questionEntity);
            }
            Integer questionInsertResult = questionsMapper.batchInsertQuestions(questionsList);
            //更新题库更新时间
            if (questionInsertResult > 0) {
                Integer totalCount = questionsMapper.getTotalCountByLibraryId(libraryId);
                questionsLibraryMapper.updateQuestionLibrary(new QuestionsLibraryEntity(libraryId, totalCount, new Date()));
            }
            String companyName = companyInfoMapper.getCompanyNameById(loginUser.getCompanyID());
            String oaMessage = companyName + ":" + loginUser.getDingName() + "成功往题库" + libraryName + "插入" + questionInsertResult + "条数据,列表共有" + resultList.size() + "条数据";
            OAMessageUtil.sendTextMsgToDept(oaMessage);
            logger.info(oaMessage);
            new Thread(new ModifyUserQuestionLibraryThread(libraryId));
            return ResultJson.succResultJson(questionInsertResult);
        } catch (Exception e) {
            // JSONObject jsonObject = new JSONObject();
            jsonObject.put("total", totalnum);
            jsonObject.put("message", "fail");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Integer getQuestionCount(Integer libraryId) {
        return questionsMapper.getTotalCountByLibraryId(libraryId);
    }

    @Override
    public JSONObject reviewQuestion(Integer libraryId, Integer questionId) {
        MobileLoginUser mobileLoginUser = MobileLoginUser.getUser();
        Integer userId = mobileLoginUser.getUserID();
        Integer companyId = mobileLoginUser.getCompanyID();
        UserConfigEntity userConfigEntity = userConfigMapper.findByUserId(userId);
        Integer onlyWrong = 0;
        if (userConfigEntity != null) {
            onlyWrong = userConfigEntity.getIsOnlyWrong();
        }
        Integer returnQuestionId = 0;
        //只看错题
        if (onlyWrong != null && onlyWrong.equals(1)) {
            List<Integer> wrongAnswerQuestionList = userXQuestionsMapper.getUserwrongAnsweredQuestionList(companyId, libraryId, userId);
            if (CollectionUtils.isEmpty(wrongAnswerQuestionList)) {
                return ResultJson.errorResultJson("不可思议,居然全对");
            } else {
                if (questionId == null) {
                    returnQuestionId = wrongAnswerQuestionList.get(0);
                } else {
                    for (int i = 0; i < wrongAnswerQuestionList.size(); i++) {
                        Integer questionIndexId = wrongAnswerQuestionList.get(i);
                        //最后一题的下一题
                        if (i == wrongAnswerQuestionList.size() - 1 && !questionId.equals(questionIndexId)) {
                            returnQuestionId = wrongAnswerQuestionList.get(0);
                            break;
                        } else if (questionId.equals(questionIndexId)) {
                            if (i != wrongAnswerQuestionList.size() - 1) {
                                returnQuestionId = wrongAnswerQuestionList.get(i + 1);
                                break;
                            } else {
                                returnQuestionId = wrongAnswerQuestionList.get(0);
                                break;
                            }
                        }
                    }
                }
            }
            //所有都看
        } else {
            //按顺序
            List<Integer> questionIdList = questionsMapper.getOrderQuestionIdsByLibrary(libraryId);
            if (questionId == null && questionIdList != null) {
                returnQuestionId = questionIdList.get(0);
            } else {
                if (CollectionUtils.isNotEmpty(questionIdList)) {
                    for (int i = 0; i < questionIdList.size(); i++) {
                        Integer questionIndexId = questionIdList.get(i);
                        if (i == questionIdList.size() - 1 && !questionId.equals(questionIndexId)) {
                            returnQuestionId = questionIdList.get(0);
                            break;
                        } else if (questionId.equals(questionIndexId)) {
                            if (i != questionIdList.size() - 1) {
                                returnQuestionId = questionIdList.get(i + 1);
                                break;
                            } else {
                                returnQuestionId = questionIdList.get(0);
                                break;
                            }
                        }
                    }
                }
            }
        }

        QuestionDetailVO questionDetailVO = getQuestionDetail(returnQuestionId);
        Integer wrongCount = 0;
        if (returnQuestionId != null) {
            wrongCount = userXQuestionsMapper.getWrongAnswerCountByUserId(companyId, returnQuestionId, userId);
        }
        QuestionReviewVO questionViewVO = new QuestionReviewVO();
        try {
            BeanUtils.copyProperties(questionViewVO, questionDetailVO);
            if (!returnQuestionId.equals(0)) {
                questionViewVO.setQuestionType(questionDetailVO.getType());
                questionViewVO.setWrongCount(wrongCount);
            }else{
                return ResultJson.errorResultJson("没有数据");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return ResultJson.succResultJson(questionViewVO);
    }

    @Override
    public JSONObject preReviewQuestion(Integer libraryId, Integer questionId) {
        MobileLoginUser mobileLoginUser = MobileLoginUser.getUser();
        Integer userId = mobileLoginUser.getUserID();
        Integer companyId = mobileLoginUser.getCompanyID();
        UserConfigEntity userConfigEntity = userConfigMapper.findByUserId(userId);
        Integer onlyWrong = 0;
        if (userConfigEntity != null) {
            onlyWrong = userConfigEntity.getIsOnlyWrong();
        }
        Integer returnQuestionId = 0;
        //只看错题
        if (onlyWrong != null && onlyWrong.equals(1)) {
            List<Integer> wrongAnswerQuestionList = userXQuestionsMapper.getUserwrongAnsweredQuestionList(companyId, libraryId, userId);
            if (CollectionUtils.isEmpty(wrongAnswerQuestionList)) {
                return ResultJson.errorResultJson("不可思议,居然全对");
            } else {
                for (int i = 0; i < wrongAnswerQuestionList.size(); i++) {
                    Integer questionIndexId = wrongAnswerQuestionList.get(i);
                    //第一题的上一题
                    if (questionId.equals(questionIndexId)) {
                        if (i != 0) {
                            returnQuestionId = wrongAnswerQuestionList.get(i - 1);
                            break;
                        } else {
                            returnQuestionId = wrongAnswerQuestionList.get(wrongAnswerQuestionList.size() - 1);
                            break;
                        }
                    } else if (i == wrongAnswerQuestionList.size() - 1 && !questionId.equals(questionIndexId)) {
                        returnQuestionId = wrongAnswerQuestionList.get(0);
                        break;
                    }
                }
            }
            //所有都看
        } else {
            //按顺序
            List<Integer> questionIdList = questionsMapper.getOrderQuestionIdsByLibrary(libraryId);
            for (int i = 0; i < questionIdList.size(); i++) {
                Integer questionIndexId = questionIdList.get(i);
                if (questionId.equals(questionIndexId)) {
                    if (i != 0) {
                        returnQuestionId = questionIdList.get(i - 1);
                        break;
                    } else {
                        returnQuestionId = questionIdList.get(questionIdList.size() - 1);
                        break;
                    }
                } else if (i == questionIdList.size() - 1 && !questionId.equals(questionIndexId)) {
                    returnQuestionId = questionIdList.get(0);
                    break;
                }
            }
        }
        QuestionDetailVO questionDetailVO = getQuestionDetail(returnQuestionId);
        Integer wrongCount = 0;
        if (questionId != null) {
            wrongCount = userXQuestionsMapper.getWrongAnswerCountByUserId(companyId, returnQuestionId, userId);
        }
        QuestionReviewVO questionViewVO = new QuestionReviewVO();
        try {
            BeanUtils.copyProperties(questionViewVO, questionDetailVO);
            if (!returnQuestionId.equals(0)) {
                questionViewVO.setQuestionType(questionDetailVO.getType());
                questionViewVO.setWrongCount(wrongCount);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return ResultJson.succResultJson(questionViewVO);
    }

    private static String getValue(HSSFCell hssfCell) {
        if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
            // 返回布尔类型的值
            return String.valueOf(hssfCell.getBooleanCellValue());
        } else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
            // 返回数值类型的值
            return String.valueOf(hssfCell.getNumericCellValue());
        } else {
            // 返回字符串类型的值
            String cellValue = hssfCell.getStringCellValue();
            if (cellValue.indexOf("(") > 0 || cellValue.indexOf(")") > 0) {
                cellValue = replaceChar(cellValue, "(", "（");
                cellValue = replaceChar(cellValue, ")", "）");
            }
            return cellValue;
        }
    }

    public static String replaceChar(String str, String char_orgi, String char_change) {
        if (str.indexOf(char_orgi) >= 0) {
            str = new String(str.replaceAll(char_orgi, char_change));
            System.out.println("str:" + str);
        }
        return str;
    }

    private QuestionDetailVO getQuestionDetail(Integer questionId) {
        QuestionsEntity questionsEntity = questionsService.getById(questionId);
        QuestionDetailVO questionDetailVO = new QuestionDetailVO();
        try {
            if (questionsEntity != null) {
                BeanUtils.copyProperties(questionDetailVO, questionsEntity);
                if (!questionsEntity.getType().equals(QuestionTypeEnum.COMPLETION.getType())) {
                    JSONObject optionsJson = JSONObject.parseObject(questionsEntity.getOptions());
                    if (questionsEntity.getLabelId() != null) {
                        String label = questionLabelService.getLabelNameById(questionsEntity.getLabelId());
                        questionDetailVO.setLabel(label);
                    }
                    String optionA = optionsJson.getString("A");
                    String optionB = optionsJson.getString("B");
                    questionDetailVO.setOptionA(optionA);
                    questionDetailVO.setOptionB(optionB);
                    if (StringUtils.isNotEmpty(optionsJson.getString("C"))) {
                        String optionC = optionsJson.getString("C");
                        questionDetailVO.setOptionC(optionC);
                    }
                    if (StringUtils.isNotEmpty(optionsJson.getString("D"))) {
                        String optionD = optionsJson.getString("D");
                        questionDetailVO.setOptionD(optionD);
                    }
                    questionDetailVO.setAnswer(null);
                } else {
                    String description = questionDetailVO.getDescription();
                    JSONArray answerArray = JSONArray.parseArray(questionDetailVO.getAnswer());
                    String completeDescription = QuestionUtil.parseCompleteDescription(description, answerArray);
                    questionDetailVO.setDescription(completeDescription);
                }
            } else {
                questionDetailVO.setId(0);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return questionDetailVO;
    }


    private static String parseChoiceToJson(QuestionExcelDTO QuestionDto) {
        JSONObject jsonObject = new JSONObject();
        if (QuestionDto.getType().equals(1) || QuestionDto.getType().equals(2)) {
            if (StringUtils.isNotEmpty(QuestionDto.getOptionA())) jsonObject.put("A", QuestionDto.getOptionA());
            else jsonObject.put("A", "");
            if (StringUtils.isNotEmpty(QuestionDto.getOptionB())) jsonObject.put("B", QuestionDto.getOptionB());
            else jsonObject.put("B", "");
            if (StringUtils.isNotEmpty(QuestionDto.getOptionC())) jsonObject.put("C", QuestionDto.getOptionC());
            else jsonObject.put("C", "");
            if (StringUtils.isNotEmpty(QuestionDto.getOptionD())) jsonObject.put("D", QuestionDto.getOptionD());
            else jsonObject.put("D", "");
        } else if (QuestionDto.getType().equals(3)) {
            jsonObject.put("A", "正确");
            jsonObject.put("B", "错误");
        } else if (QuestionDto.getType().equals(4)) {
            return "";
        }
        return jsonObject.toJSONString();
    }


    public Integer getDivideResult(Integer a1, Integer a2) {
        BigDecimal result = new BigDecimal(a1).divide(new BigDecimal(a2), 2, BigDecimal.ROUND_HALF_UP);
        return new Integer(new BigDecimal(100).multiply(result).intValue());
    }


    public static void main(String[] args) {
//        List<Integer> list = new ArrayList<>();
//        for (int i = 0; i < 100; i++) {
//            list.add(i);
//        }
//        for (int i = 0; i < list.size(); i++) {
//            if(list.get(i)%1==0){
//                list.remove(i);
//            }
//        }
//        for(int i = 0; i < list.size(); i++){
//            System.out.println(list.get(i));
//        }
//        Integer libraryId = 1;
//        AssertUtil.isTrue(libraryId != null, "不存在的题库id");
//        String str = ResultJson.finishResultJson(ResultCodeEnum.KICKED_OUT.getValue(), "该题库已学习完毕", new UserXTitleSimpleVO("头脑王者", 1, "http://5.jpg")).toString();
//        System.out.println(str);
//        Integer a = new Integer(1);
//        Integer c = new Integer(3);
//        BigDecimal result = new BigDecimal(a).divide(new BigDecimal(c), 2, BigDecimal.ROUND_HALF_UP);
//        System.out.println(new BigDecimal(100).multiply(result).intValue());
//        JSONArray jsonArray = new JSONArray();
//        jsonArray.add("春|spring");
//        jsonArray.add("夏|summer");
//        jsonArray.add("秋|autumn");
//        jsonArray.add("冬|winter");
//        System.out.println(jsonArray.toString());
//        String str = "[2019|猪],[春|spring],[2019,在呼唤你|2019在呼唤你]";
//        JSONArray jsonArray = QuestionUtil.parseBlankTOJsonArray(str);
//        System.out.println(jsonArray.toString());
//        System.out.println("---------------------");
//        String result = QuestionUtil.parseJsonToString(jsonArray);
//        System.out.println(result);

//        String answer = "A|B|C";
//        String answer1 = answer.replaceAll("\\|", ",");
//        System.out.println(answer1);

        String options = "{\"A\":\"动火证未经批准，禁止动火。\",\"B\":\"不与生产系统可靠隔绝，禁止动火。\",\"C\":\"不消除周围易燃物，禁止动火。\",\"D\":\"没有消防措施，禁止动火。\"}";
        JSONObject optionsJson = JSONObject.parseObject(options);
        System.out.println(optionsJson.toString());
    }
}
