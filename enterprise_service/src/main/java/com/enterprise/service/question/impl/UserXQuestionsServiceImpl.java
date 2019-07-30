package com.enterprise.service.question.impl;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.*;
import com.enterprise.base.entity.*;
import com.enterprise.base.enums.*;
import com.enterprise.base.exceptions.BusinessException;
import com.enterprise.base.vo.*;
import com.enterprise.base.vo.bagtool.BagDropVO;
import com.enterprise.base.vo.dto.QuestionAnswerDto;
import com.enterprise.base.vo.dto.QuestionAnswerDto2;
import com.enterprise.base.vo.rank.*;
import com.enterprise.mapper.bagTool.BagToolEffectMapper;
import com.enterprise.mapper.bagTool.BagToolEffectQuestionsMapper;
import com.enterprise.mapper.department.DepartmentMapper;
import com.enterprise.mapper.paperBall.PaperBallMapper;
import com.enterprise.mapper.pet.MyPetMapper;
import com.enterprise.mapper.pet.PetExperienceMapper;
import com.enterprise.mapper.pet.PetWeightMapper;
import com.enterprise.mapper.petFood.PetFoodDetailMapper;
import com.enterprise.mapper.petFood.PetFoodMapper;
import com.enterprise.mapper.questions.QuestionLibraryTitleMapper;
import com.enterprise.mapper.questions.QuestionsLibraryMapper;
import com.enterprise.mapper.questions.QuestionsMapper;
import com.enterprise.mapper.questions.UserXQuestionsMapper;
import com.enterprise.mapper.users.*;
import com.enterprise.service.isvTickets.IsvTicketsService;
import com.enterprise.service.question.QuestionsService;
import com.enterprise.service.question.UserXLibraryService;
import com.enterprise.service.question.UserXQuestionsService;
import com.enterprise.service.user.UserXDeptService;
import com.enterprise.service.user.UserXTitleService;
import com.enterprise.util.AssertUtil;
import com.enterprise.util.DateUtil;
import com.enterprise.util.QuestionUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by anson on 18/4/2.
 */
@Service
public class UserXQuestionsServiceImpl implements UserXQuestionsService {


    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private UserXCompanyMapper userXCompanyMapper;

    @Resource
    private UserXQuestionsMapper userXQuestionsMapper;

    @Resource
    private QuestionsMapper questionsMapper;

    @Resource
    private QuestionsService questionsService;

    @Resource
    private UserMapper userMapp;

    @Resource
    private MyPetMapper myPetMapper;

    @Resource
    private PetExperienceMapper petExperienceMapper;

    @Resource
    private UserXLibraryService userXLibraryService;

    @Resource
    private UserXDeptService userXDeptService;

    @Resource
    private UserXTitleService userXTitleService;

    @Resource
    private BagToolEffectMapper bagToolEffectMapper;

    @Resource
    private BagToolEffectQuestionsMapper bagToolEffectQuestionsMapper;

    @Resource
    private UserXLibraryMapper userXLibraryMapper;

    //======================================================================================

    @Resource
    private DepartmentMapper departmentMapper;

    @Resource
    private IsvTicketsService isvTicketsService;

    @Resource
    private UserXOpenMapper userXOpenMapper;

    @Resource
    private QuestionsLibraryMapper questionLibraryMapper;

    @Resource
    private QuestionLibraryTitleMapper questionLibraryTitleMapper;

    @Resource
    private PetWeightMapper petWeightMapper;

    @Resource
    private PetFoodMapper petFoodMapper;

    @Resource
    private PetFoodDetailMapper petFoodDetailMapper;

    @Resource
    private PaperBallMapper paperBallMapper;

    @Resource
    private UserXTitleMapper userXTitleMapper;

    private Integer SEAL_COUNT = BagToolConstant.SEAL_COUNT;

    private Integer DECAY_COUNT = BagToolConstant.DECAY_COUNT;

    private Integer EVERY_GAIN_COUNT = PetConstant.EVERY_GAIN_COUNT;

    private String USERQUESTION = "userxquestion";

    private final String DEFAULT_TITLE = "未获得";

    private final String NO_TITLE = "暂无头衔";

    @Override
    public Integer createOrUpdateUserXQestion(UserXQuestionsEntity userXQuestionsEntity) {
        if (userXQuestionsEntity.getId() == null || (userXQuestionsEntity.getId() != null && userXQuestionsEntity.getId().equals(0))) {
            userXQuestionsEntity.setCreateTime(new Date());
            userXQuestionsEntity.setUpdateTime(new Date());
            if (userXQuestionsEntity.getAnswerTime() != null) {
                return userXQuestionsMapper.createOrUpdateUserXQuestion(userXQuestionsEntity);
            }
            return userXQuestionsMapper.createOrUpdateUserXQuestionNew(userXQuestionsEntity);
        } else {
            userXQuestionsEntity.setUpdateTime(new Date());
            return userXQuestionsMapper.updateUserXQuestion(userXQuestionsEntity);
        }
    }

    private JSONObject getAnswerResultJson(Integer type, String avatar, QuestionAnswerDto2 questionAnswerDto2, Integer questionId, String answer, String answerDesc, Integer answerStatus, String message, Integer questionCount, Integer correctAnswerCount, Integer petFoodResult) {
        QuestionAnswerVO questionAnswerVO = new QuestionAnswerVO(questionId, type, answer, answerStatus, message, answerDesc, questionCount, correctAnswerCount);
        UserXTitleSimpleVO userXTitleSimpleVO = new UserXTitleSimpleVO();
        BagDropVO dropBoxVO = new BagDropVO(0, 0);
        if (answerStatus.equals(QuestionAnswerEnum.RIGHT.getValue())) {
            QuestionLibraryTitleEntity questionLibraryTitleEntity = questionLibraryTitleMapper.getQuestionTitleByLibrary(questionAnswerDto2.getLibraryId());
            if (questionLibraryTitleEntity != null && correctAnswerCount >= questionCount) {
                userXTitleSimpleVO.setTitle(questionLibraryTitleEntity.getTitle());
                userXTitleSimpleVO.setAvatar(avatar);
                userXTitleSimpleVO.setTitleType(questionLibraryTitleEntity.getType());
            }
            //dropBoxVO = bagToolService.dropBox(2);
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("answer", questionAnswerVO);
        resultMap.put("petFood", petFoodResult);
        resultMap.put("title", userXTitleSimpleVO);
        resultMap.put("dropBox", dropBoxVO);
        return ResultJson.succResultJson(resultMap);
    }

    private Integer getAnswerStatus(String answer, QuestionsEntity questionsEntity) {

        Boolean answerMatch = QuestionUtil.answerMatchInsert(answer);
        if (questionsEntity.getType().equals(QuestionTypeEnum.MULTI_CHOICE.getType()) && !answerMatch) {
            return 2;
        }
        Integer type = questionsEntity.getType();
        String rightAnswer = questionsEntity.getAnswer().trim();
        logger.info("answer:" + answer + "---rightAnswer:" + rightAnswer);
        if (type.equals(1) || type.equals(3)) {
            if (answer.equals(rightAnswer)) {
                return QuestionAnswerEnum.RIGHT.getValue();
            } else {
                return QuestionAnswerEnum.WRONG.getValue();
            }
        } else if (type.equals(2)) {
            answer = QuestionUtil.answerChange(answer).trim();
            if (answer.equals(rightAnswer)) {
                return QuestionAnswerEnum.RIGHT.getValue();
            } else {
                return QuestionAnswerEnum.WRONG.getValue();
            }
        } else if (type.equals(4)) {
            System.out.println(questionsEntity.getAnswer());
//            JSONArray rightAnswerArray = QuestionUtil.parseBlankTOJsonArray(questionsEntity.getAnswer());
            JSONArray rightAnswerArray = JSONArray.parseArray(questionsEntity.getAnswer());
            JSONArray answerArray = QuestionUtil.parseBlankTOJsonArray(answer);
            if (answerArray == null || rightAnswerArray == null || rightAnswerArray.size() != answerArray.size()) {
                return QuestionAnswerEnum.WRONG.getValue();
            } else {
                //按顺序判断
                for (int i = 0; i < rightAnswerArray.size(); i++) {
                    String rightAnswerIndexI = rightAnswerArray.getString(i);//数据库
                    String answerIndexI = answerArray.getString(i);//用户传值
                    String[] rightAnswerArr = rightAnswerIndexI.split("\\|");
                    for (int j = 0; j < rightAnswerArr.length; j++) {
                        if (answerIndexI.equals(rightAnswerArr[j])) {
                            break;
                        } else if (!answerIndexI.equals(rightAnswerArr[j]) && j == rightAnswerArr.length - 1) {
                            return QuestionAnswerEnum.WRONG.getValue();
                        } else {
                            continue;
                        }
                    }
                }
                return QuestionAnswerEnum.RIGHT.getValue();
            }
        }
        return QuestionAnswerEnum.WRONG.getValue();
    }


    /**
     * 用户答题
     */
    @Override
    public JSONObject answer(QuestionAnswerDto questionAnswerDto, Integer effedcId) throws BusinessException {
        MobileLoginUser mobileLoginUser = MobileLoginUser.getUser();
        QuestionsEntity questionsEntity = questionsService.getById(questionAnswerDto.getQuestionId());
        Integer libraryId = questionsEntity.getLibraryId();
        UserXQuestionsEntity userXQuestionsEntity = new UserXQuestionsEntity();
        userXQuestionsEntity.setCompanyId(mobileLoginUser.getCompanyID());
        userXQuestionsEntity.setUserId(mobileLoginUser.getUserID());
        userXQuestionsEntity.setQuestionId(questionAnswerDto.getQuestionId());
        //根据题型判断是否正确
        Integer answerStatus = getAnswerStatus(questionAnswerDto.getAnswer(), questionsEntity);
        userXQuestionsEntity.setAnswerStatus(answerStatus);
        userXQuestionsEntity.setType(questionAnswerDto.getType());
        userXQuestionsEntity.setStatus(StatusEnum.OK.getValue());
        if (saveUserXQuestions(userXQuestionsEntity) < 0) {
            logger.error("保存用户答题状态失败!");
        }

        Integer questionCount = questionsMapper.getTotalCountByLibraryId(libraryId);
        Integer correctAnsweredQuestionNumber = userXQuestionsMapper.getUserCorrectQuestionsByLibraryId(mobileLoginUser.getCompanyID(), mobileLoginUser.getUserID(), libraryId);

//        MyPetVO myPetVO = myPetMapper.getMyPet(mobileLoginUser.getUserID());
//        if (myPetVO.getExperienceValue() == null) {
//            myPetVO.setExperienceValue(0);
//        }
        if (userXQuestionsEntity.getAnswerStatus().equals(QuestionAnswerEnum.RIGHT.getValue())) {
            QuestionAnswerDto2 questionAnswerDto2 = new QuestionAnswerDto2(mobileLoginUser.getCompanyID(), questionsEntity.getLibraryId(), mobileLoginUser.getUserID());
            //答题正确宠物需要升级--使用道具
            Integer petFoodResult = updatePetExperience_tool(userXQuestionsEntity, mobileLoginUser, effedcId);
            return getAnswerResultJson(userXQuestionsEntity.getType(), mobileLoginUser.getAvatar(), questionAnswerDto2, questionAnswerDto.getQuestionId(), questionsEntity.getAnswer(), questionsEntity.getAnswerDesc(), userXQuestionsEntity.getAnswerStatus(), "答题正确", questionCount, correctAnsweredQuestionNumber, petFoodResult);
        } else if (userXQuestionsEntity.getAnswerStatus().equals(QuestionAnswerEnum.WRONG.getValue())) {
            return getAnswerResultJson(userXQuestionsEntity.getType(), mobileLoginUser.getAvatar(), null, questionAnswerDto.getQuestionId(), questionsEntity.getAnswer(), questionsEntity.getAnswerDesc(), userXQuestionsEntity.getAnswerStatus(), "答题错误", questionCount, correctAnsweredQuestionNumber, 0);
        } else {
            return getAnswerResultJson(userXQuestionsEntity.getType(), mobileLoginUser.getAvatar(), null, questionAnswerDto.getQuestionId(), questionsEntity.getAnswer(), questionsEntity.getAnswerDesc(), userXQuestionsEntity.getAnswerStatus(), "答题超时", questionCount, correctAnsweredQuestionNumber, 0);
        }
    }


    /**
     * 用户答题
     */
    @Override
    public JSONObject reviewAnswer(QuestionAnswerDto questionAnswerDto) throws BusinessException {
        QuestionsEntity questionsEntity = questionsService.getById(questionAnswerDto.getQuestionId());
        //根据题型判断是否正确
        String messag = "";
        Integer answerStatus = getAnswerStatus(questionAnswerDto.getAnswer(), questionsEntity);
        if (answerStatus.equals(QuestionAnswerEnum.RIGHT.getValue())) {
            messag = "回答正确";
        } else {
            messag = "回答错误";
        }
        QuestionReviewAnswerVO questionAnswerVO = new QuestionReviewAnswerVO(questionAnswerDto.getQuestionId(), questionsEntity.getType(), questionAnswerDto.getAnswer(), questionsEntity.getAnswer(), answerStatus, questionsEntity.getAnswerDesc(), messag);
        return ResultJson.succResultJson(questionAnswerVO);
    }

    @Override
    public JSONObject answerTest(Integer companyId, Integer userId, Integer questionId, String answer, Integer
            type) {
        try {
            QuestionsEntity questionsEntity = questionsService.getById(questionId);
            UserXQuestionsEntity userXQuestionsEntity = new UserXQuestionsEntity(companyId, userId, questionId, type, null, null, null, StatusEnum.OK.getValue(), new Date(), new Date());
            if (StringUtils.isEmpty(answer)) {
                userXQuestionsEntity.setAnswerStatus(QuestionAnswerEnum.NO_ANSWER.getValue());
            } else if (answer.equals(questionsEntity.getAnswer())) {
                userXQuestionsEntity.setAnswerStatus(QuestionAnswerEnum.RIGHT.getValue());
            } else {
                userXQuestionsEntity.setAnswerStatus(QuestionAnswerEnum.WRONG.getValue());
            }
            if (saveUserXQuestionsTest(userXQuestionsEntity) < 0) {
                return ResultJson.errorResultJson("保存失败");
            }
            return ResultJson.succResultJson(userXQuestionsEntity);
        }catch (Exception e){
            return ResultJson.succResultJson("小错误");
        }
    }

    /**
     * 更新用户答题
     */

    private Integer saveUserXQuestionsTest(UserXQuestionsEntity userXQuestionsEntity) {
        Integer companyId = userXQuestionsEntity.getCompanyId();
        Integer userId = userXQuestionsEntity.getUserId();
        QuestionsEntity questionsEntity = questionsService.getById(userXQuestionsEntity.getQuestionId());
        Integer libraryId = questionsEntity.getLibraryId();
        if (userXQuestionsEntity.getAnswerStatus().equals(QuestionAnswerEnum.RIGHT.getValue())) {
            Integer schedule = Integer.valueOf(0);
            Integer result = userXQuestionsMapper.getIsCorrectedAnsweredBefore(companyId, userId, userXQuestionsEntity.getQuestionId());
            //获取正确回答的问题数
            Integer correctAnsweredQuestionNumber = userXQuestionsMapper.getUserCorrectQuestionsByLibraryId(companyId, userId, libraryId);
            Integer totoalCountInLibrary = questionsMapper.getTotalCountByLibraryId(libraryId);
            //回答过的题进度不增加
        } else {
            //回答错误更新最新回答时间
        }
        return 1;
    }

    @Override
    public Integer getTotalAnsweredByCompanyIdAndDate(Integer companyId, String date) {
        return userXQuestionsMapper.getTotalAnsweredByCompanyIdAndDate(companyId, date);
    }

    @Override
    public Integer synchronizeLibrary(String corpId) {
        Integer totalCount = 0;
        Integer companyId = isvTicketsService.getIsvTicketByCorpId(corpId).getCompanyId();
        List<QuestionsLibraryEntity> questionLibraryList = questionLibraryMapper.findQuestionLibrary(new QuestionsLibraryEntity(companyId, 0));
        List<UserVO> userCompanyList = userXCompanyMapper.getUserByCorpId(corpId, StatusEnum.OK.getValue());
        List<UserXLibraryEntity> resultUserXLibraryList = new ArrayList<>();
        for (int i = 0; i < questionLibraryList.size(); i++) {
            Integer libraryId = questionLibraryList.get(i).getId();
            Integer totalQuestionCount = questionsMapper.getTotalCountByLibraryId(libraryId);
            for (int j = 0; j < userCompanyList.size(); j++) {
                Integer userId = userCompanyList.get(j).getId();
                UserXLibraryEntity usLibrary = userXLibraryService.getByUserIdAndLibraryId(companyId, userId, libraryId);
                if (usLibrary != null) {
                    BigDecimal schedule = new BigDecimal(0);
                    DecimalFormat format = new DecimalFormat("0");
                    Integer correctCount = userXQuestionsMapper.getTotalCorrectCountByLibraryId(companyId, libraryId, userId);
                    if (totalQuestionCount > 0 && correctCount >= totalQuestionCount) {
                        schedule = new BigDecimal(100);
                    } else if (totalQuestionCount > 0) {
                        schedule = new BigDecimal(QuestionUtil.getPercentDivision(correctCount, totalQuestionCount));
                    } else if (totalQuestionCount == 0) {
                        schedule = new BigDecimal(0);
                    }
                    if (usLibrary.getSchedule() == null || (usLibrary.getSchedule() != null && !usLibrary.getSchedule().equals(schedule))) {
                        totalCount++;
                        // public UserXLibraryEntity(Integer id, Integer userId, Integer libraryId, Integer schedule, Date lastAnswerTime, Integer status, Date createTime, Date updateTime) {
                        resultUserXLibraryList.add(new UserXLibraryEntity(usLibrary.getId(), userId, libraryId, schedule, null, null, null, null));
                        //logger.info("userId:"+userId + " libraryId:"+libraryId+" schedule:"+schedule);
                    }

                }
            }
        }
        for (int i = 0; i < resultUserXLibraryList.size(); i++) {
            logger.info(resultUserXLibraryList.get(i).toString());
        }

        return totalCount;
    }

    @Override
    public Integer batchUpdateStatus(Integer questionId, Integer status) {
        return userXQuestionsMapper.batchUpdateStatus(questionId, status);
    }

    @Override
    public List<UserAnswerRankSimpleNewVO> getTop5Rank(Integer companyId) {
        IsvTicketsEntity isvTicketsEntity = isvTicketsService.getIsvTicketByCompanyId(companyId);
        String corpId = isvTicketsEntity.getCorpId();
        //获取有宠物重量的用户
        List<Integer> petWeightUserList = petWeightMapper.getPetWeightUserList(corpId);
        List<Integer> remainUserList = userXCompanyMapper.getRemainUserIdList2(corpId, petWeightUserList);
        List<UserPetWeightRankVO> petWeightRankList = null;
        if (CollectionUtils.isNotEmpty(petWeightUserList)) {
            petWeightRankList = petWeightMapper.getRankWeightListFive(companyId, petWeightUserList, remainUserList);
        } else {
            petWeightRankList = petWeightMapper.getRankOtherListFive(companyId, remainUserList);
        }
        List<UserAnswerRankSimpleNewVO> userWeekRankList = new ArrayList<>();
        for (int i = 0; i < petWeightRankList.size(); i++) {
            Integer rank = i + 1;
            UserPetWeightRankVO userPetWeightRankVO = petWeightRankList.get(i);
            UserAnswerRankSimpleNewVO userAnswerRankSimpleNewVO = new UserAnswerRankSimpleNewVO();
            Integer rankUserId = userPetWeightRankVO.getUserId();
            UserEntity userEntity = userMapp.getUserById(rankUserId);
            Integer departmentId = userXDeptService.getDepartmentId(corpId, rankUserId);
            String deptname = departmentMapper.getDeptNameById(companyId, departmentId);
//            String userTitle = userXTitleService.findUseWearTitleByCompany(companyId, userAnswerRankVO.getUserId());
            UserXTitleVO userTitle = userXTitleService.findUserWearTitleByCompany(companyId, rankUserId);
            Integer existPaperBallCount = paperBallMapper.getExistPaperBallCount(companyId, rankUserId);

            userAnswerRankSimpleNewVO.setName(userEntity.getName() != null ? userEntity.getName() : "");
            userAnswerRankSimpleNewVO.setHeadImage(userEntity.getAvatar() != null ? userEntity.getAvatar() : "");
            userAnswerRankSimpleNewVO.setRank(rank);
            userAnswerRankSimpleNewVO.setDeptName(deptname);
            MyPetVO myPetVO = myPetMapper.getMyPet(rankUserId);
            if (userTitle != null) {
                userAnswerRankSimpleNewVO.setTitle(userTitle.getTitle());
                userAnswerRankSimpleNewVO.setTitleType(userTitle.getType());
            }
            if (existPaperBallCount > 0) {
                userAnswerRankSimpleNewVO.setPaperBall(1);
            } else {
                userAnswerRankSimpleNewVO.setPaperBall(0);
            }
            if (myPetVO != null) {
                userAnswerRankSimpleNewVO.setIsNewUser(0);
            } else {
                userAnswerRankSimpleNewVO.setIsNewUser(1);
            }

            PetWeightEntity petWeightEntity = petWeightMapper.getPetWeight(companyId, rankUserId);
            if (petWeightEntity != null) {
                userAnswerRankSimpleNewVO.setWeight(petWeightEntity.getWeight());
            } else {
                userAnswerRankSimpleNewVO.setWeight(0);
            }
        }
        return userWeekRankList;
    }

    @Override
    public List<UserXQuestionsEntity> getCorrectAnswerUserList(Integer companyId, Integer questionId) {
        return userXQuestionsMapper.getCorrectAnswerUserList(null, questionId);
    }

    @Override
    public JSONObject getHourUserAnswerList() {
        List<String> resultList = userXQuestionsMapper.getHourUserAnswerList(EVERY_GAIN_COUNT);
        if(CollectionUtils.isEmpty(resultList)){
            resultList = userXQuestionsMapper.getHourUserAnswerList2(EVERY_GAIN_COUNT);
        }
        return ResultJson.succResultJson(resultList);
    }

    /**
     * 需要增加正确回答数
     * 更新用户答题
     */
    private Integer saveUserXQuestions(UserXQuestionsEntity userXQuestionsEntity) {
        Integer companyId = userXQuestionsEntity.getCompanyId();
        Integer userId = userXQuestionsEntity.getUserId();
        QuestionsEntity questionsEntity = questionsService.getById(userXQuestionsEntity.getQuestionId());
        Integer libraryId = questionsEntity.getLibraryId();
        if (userXQuestionsEntity.getAnswerStatus().equals(QuestionAnswerEnum.RIGHT.getValue())) {
            BigDecimal schedule = new BigDecimal(0);
            Integer result = userXQuestionsMapper.getIsCorrectedAnsweredBefore(companyId, userId, userXQuestionsEntity.getQuestionId());
            //获取正确回答的问题数
            Integer correctAnsweredQuestionNumber = userXQuestionsMapper.getUserCorrectQuestionsByLibraryId(companyId, userId, libraryId);
            Integer totoalCountInLibrary = questionsMapper.getTotalCountByLibraryId(libraryId);
            //回答过的题进度不增加
            if (totoalCountInLibrary > 0 && result <= 0 && userXQuestionsEntity.getType().equals(0)) {
                correctAnsweredQuestionNumber += 1;
                DecimalFormat format = new DecimalFormat("0");
                //进度重新计算
                schedule = new BigDecimal(QuestionUtil.getPercentDivision(correctAnsweredQuestionNumber, totoalCountInLibrary));
                if (schedule.compareTo(new BigDecimal(100)) > 0) schedule = new BigDecimal(100);
                UserXLibraryEntity userXLibraryEntity = userXLibraryService.getByUserIdAndLibraryId(companyId, userId, libraryId);
                if (userXLibraryEntity == null) {
                    userXLibraryEntity = new UserXLibraryEntity(userId, libraryId, schedule, new Date(), StatusEnum.OK.getValue(), new Date(), new Date(), companyId);
                    if (schedule.equals(new BigDecimal(100))) {
                        userXLibraryEntity.setFinishTime(DateUtil.getDisplayYMDHMS(new Date()));
                    }
                    userXLibraryService.createOrUpdateUserXLibrary(userXLibraryEntity);
                } else {
                    userXLibraryEntity.setLastAnswerTime(new Date());
                    userXLibraryEntity.setSchedule(schedule);
                    userXLibraryEntity.setUpdateTime(new Date());
                    if (schedule.equals(new BigDecimal(100))) {
                        userXLibraryEntity.setFinishTime(DateUtil.getDisplayYMDHMS(new Date()));
                    }
                    userXLibraryService.createOrUpdateUserXLibrary(userXLibraryEntity);
                }
                //更新用户头衔
                if (schedule.compareTo(new BigDecimal(100)) == 0) {
                    //如果有头衔则更新
                    QuestionLibraryTitleEntity questionLibraryTitleInDB = questionLibraryTitleMapper.getQuestionTitleByLibrary(libraryId);
//                    logger.info("questionLibraryTitle:" + questionLibraryTitleInDB.toString());
                    if (questionLibraryTitleInDB != null && questionLibraryTitleInDB.getStatus() != null && questionLibraryTitleInDB.getStatus().equals(StatusEnum.OK.getValue())) {
                        UserXTitleEntity userXTitleEntity = new UserXTitleEntity(companyId, libraryId, userId, questionLibraryTitleInDB.getId(), 0, StatusEnum.OK.getValue(), new Date(), new Date());
                        userXTitleService.saveOrUpdateUserXTitle(userXTitleEntity);
                    }
                }

            }
        } else {
            //回答错误更新最新回答时间
            UserXLibraryEntity userXLibraryEntity = userXLibraryService.getByUserIdAndLibraryId(companyId, userXQuestionsEntity.getUserId(), questionsEntity.getLibraryId());
            if (userXLibraryEntity != null) {
                userXLibraryService.createOrUpdateUserXLibrary(new UserXLibraryEntity(userXLibraryEntity.getId(), new Date(), new Date()));
            }
        }
        return createOrUpdateUserXQestion(userXQuestionsEntity);
    }


    /**
     * 更新宠物经验
     private Integer updatePetExperience_tool(UserXQuestionsEntity userXQuestionsEntity, MobileLoginUser mobileLoginUser, Integer exp) {
     Integer userId = mobileLoginUser.getUserID();
     Integer companyId = mobileLoginUser.getCompanyID();
     MyPetVO myPetVO = myPetMapper.getMyPet(userId);
     if (myPetVO != null) {
     Integer level = myPetVO.getLevel();
     //判断是否使用道具 1>是否有丘比特 2>是否小衰神(答2题算1题) 3>是否有封印咒(15题解封)
     //判断是否使用道具 1>是否有丘比特 2>是否小衰神(不获得猫粮) 3>是否有封印咒(10题解封)
     List<BagToolEffectEntity> selfCupid = bagToolEffectMapper.getUserEffectedStatusSelf(companyId, userId, BagToolTypeEnum.CUPID.getValue());
     List<BagToolEffectEntity> otherCupid = bagToolEffectMapper.getUserEffectedStatusOther(companyId, userId, BagToolTypeEnum.CUPID.getValue());
     List<BagToolEffectEntity> selfDecay = bagToolEffectMapper.getUserEffectedStatusOther(companyId, userId, BagToolTypeEnum.DECAY.getValue());
     List<BagToolEffectEntity> selfSeal = bagToolEffectMapper.getUserEffectedStatusOther(companyId, userId, BagToolTypeEnum.SEAL.getValue());
     if (CollectionUtils.isNotEmpty(selfCupid) || CollectionUtils.isNotEmpty(otherCupid) ||
     CollectionUtils.isNotEmpty(selfDecay) || CollectionUtils.isNotEmpty(selfSeal)) {
     Integer cupidUserId = null;
     Integer cupidEffeciId = null;
     if (CollectionUtils.isNotEmpty(selfCupid)) {
     cupidUserId = selfCupid.get(0).getEffectUserId();
     cupidEffeciId = selfCupid.get(0).getId();
     } else if (CollectionUtils.isNotEmpty(otherCupid)) {
     cupidUserId = otherCupid.get(0).getUserId();
     cupidEffeciId = otherCupid.get(0).getId();
     }
     //封印-经验值不减半
     if (CollectionUtils.isEmpty(selfDecay)) {
     if (CollectionUtils.isNotEmpty(selfSeal)) {
     BagToolEffectEntity bagToolEffectEntity = selfSeal.get(0);
     BagToolEffectQuestionEntity bagToolEffectQuestionEntity = new BagToolEffectQuestionEntity(companyId, BagToolTypeEnum.SEAL.getValue(), userId, userXQuestionsEntity.getId(), bagToolEffectEntity.getId(), new Date());
     bagToolEffectQuestionsMapper.createOne(bagToolEffectQuestionEntity);
     Integer effectCount = bagToolEffectQuestionsMapper.getToolEffectCount(companyId, userId, bagToolEffectEntity.getId());
     //解除封印
     if (effectCount >= SEAL_COUNT) {
     bagToolEffectMapper.dispelTool(bagToolEffectEntity.getId(), StatusEnum.DELETE.getValue());
     }
     }
     //插入用户答题记录,bag_tool_effect_question丘比特记录
     if (CollectionUtils.isNotEmpty(selfCupid) || CollectionUtils.isNotEmpty(otherCupid)) {
     //insert into user_x_questions(company_id,user_id,question_id,type,answer_status,tool_type,status, create_time, update_time)
     //(Integer companyId, Integer userId, Integer questionId, Integer type, Integer answerStatus, Integer answerTime, Integer toolType, Integer status, Date createTime, Date updateTime)
     UserXQuestionsEntity userXQuestionsToolEntity = new UserXQuestionsEntity(companyId, cupidUserId, userXQuestionsEntity.getQuestionId(), 0, StatusEnum.OK.getValue(),null,BagToolTypeEnum.SEAL.getValue(), StatusEnum.TOOL.getValue(), new Date(), new Date());
     userXQuestionsMapper.createOrUpdateUserXQuestion(userXQuestionsToolEntity);
     BagToolEffectQuestionEntity bagToolEffectQuestionEntity = new BagToolEffectQuestionEntity(companyId, BagToolTypeEnum.CUPID.getValue(), cupidUserId, userXQuestionsToolEntity.getId(), cupidEffeciId, new Date());
     bagToolEffectQuestionEntity.setAddExp(1);
     bagToolEffectQuestionsMapper.createOne(bagToolEffectQuestionEntity);
     bagToolEffectQuestionsMapper.updateOne(new BagToolEffectQuestionEntity(bagToolEffectQuestionEntity.getId(), 1));
     //双方经验都+1
     MyPetVO loverPetVO = myPetMapper.getMyPet(cupidUserId);
     updatePetExp(loverPetVO);
     return updatePetExp(myPetVO);
     } else {
     return updatePetExp(myPetVO);
     }
     //小衰神 经验值减半
     }else {
     userXQuestionsEntity.setToolType(BagToolTypeEnum.DECAY.getValue());
     userXQuestionsMapper.updateUserXQuestion(userXQuestionsEntity);
     //封印
     if (CollectionUtils.isNotEmpty(selfSeal)) {
     BagToolEffectEntity bagToolEffectEntity = selfSeal.get(0);
     BagToolEffectQuestionEntity bagToolEffectQuestionEntity = new BagToolEffectQuestionEntity(companyId, BagToolTypeEnum.SEAL.getValue(), userId, userXQuestionsEntity.getId(), bagToolEffectEntity.getId(), new Date());
     bagToolEffectQuestionsMapper.createOne(bagToolEffectQuestionEntity);
     Integer effectCount = bagToolEffectQuestionsMapper.getToolEffectCount(companyId, userId, bagToolEffectEntity.getId());
     //解除封印
     if (effectCount >= SEAL_COUNT) {
     bagToolEffectMapper.dispelTool(bagToolEffectEntity.getId(), StatusEnum.DELETE.getValue());
     }
     }
     //经验
     BagToolEffectEntity selfDecayEffectEntity = selfDecay.get(0);

     BagToolEffectQuestionEntity bagToolEffectQuestionDecay = new BagToolEffectQuestionEntity(companyId, BagToolTypeEnum.DECAY.getValue(), userId, userXQuestionsEntity.getId(), selfDecayEffectEntity.getId(), new Date());
     bagToolEffectQuestionsMapper.createOne(bagToolEffectQuestionDecay);

     Integer decayEffectCount = bagToolEffectQuestionsMapper.getToolEffectCount(companyId, userId, selfDecayEffectEntity.getId());
     Boolean addExp = false;
     Boolean cupidExp = false;
     if (decayEffectCount % 2 == 0) {
     addExp = true;
     }
     Integer effectQuestionId = 0;
     //                    bagToolEffectQuestionMapper
     //插入用户答题记录,bag_tool_effect_question丘比特记录
     if (CollectionUtils.isNotEmpty(selfCupid) || CollectionUtils.isNotEmpty(otherCupid)) {
     cupidExp = true;
     //insert into user_x_questions(company_id,user_id,question_id,type,answer_status,tool_type,status, create_time, update_time)

     }
     if (addExp) {
     //对方加经验
     if (cupidExp) {
     //小衰神
     UserXQuestionsEntity userXQuestionsToolEntity = new UserXQuestionsEntity(companyId, cupidUserId, userXQuestionsEntity.getQuestionId(), 0, StatusEnum.OK.getValue(), null, StatusEnum.TOOL.getValue(), new Date(), new Date());
     userXQuestionsMapper.createOrUpdateUserXQuestion(userXQuestionsToolEntity);
     BagToolEffectQuestionEntity bagToolEffectQuestionEntity = new BagToolEffectQuestionEntity(companyId, BagToolTypeEnum.CUPID.getValue(), cupidUserId, userXQuestionsToolEntity.getId(), cupidEffeciId, new Date());
     bagToolEffectQuestionsMapper.createOne(bagToolEffectQuestionEntity);
     effectQuestionId = bagToolEffectQuestionEntity.getId();
     MyPetVO loverPetVO = myPetMapper.getMyPet(cupidUserId);
     updatePetExp(loverPetVO);
     bagToolEffectQuestionsMapper.updateOne(new BagToolEffectQuestionEntity(effectQuestionId, 1));
     bagToolEffectQuestionDecay.setAddExp(1);
     bagToolEffectQuestionsMapper.updateOne(bagToolEffectQuestionDecay);
     }
     //非丘比特则单方加经验
     return updatePetExp(myPetVO);
     } else {
     return 0;
     }
     }
     } else {
     return updatePetExp(myPetVO);
     }
     } else {
     return 0;
     }
     }
     */


    /**
     * 0/1/2 更新失败/获得猫粮/衰神未获取猫粮
     */
    private Integer updatePetExperience_tool(UserXQuestionsEntity userXQuestionsEntity, MobileLoginUser mobileLoginUser, Integer effectId) {
        Integer userId = mobileLoginUser.getUserID();
        Integer companyId = mobileLoginUser.getCompanyID();
        MyPetVO myPetVO = myPetMapper.getMyPet(userId);
        Integer effectTooId = null;
        if (myPetVO != null) {
            if (effectId != null) {
                BagToolEffectEntity bagToolEffectEntity = bagToolEffectMapper.getById(effectId);
                if (bagToolEffectEntity != null && bagToolEffectEntity.getEffectUserId().equals(userId)) {
                    effectTooId = bagToolEffectEntity.getToolId();
                }
            }

            //判断是否使用道具 1>是否有丘比特 2>是否小衰神(答2题算1题) 3>是否有封印咒(15题解封) old
            //判断是否使用道具 1>是否有丘比特 2>是否小衰神(不获得猫粮) 3>是否有封印咒(10题解封) new
            List<BagToolEffectEntity> selfCupid = bagToolEffectMapper.getUserEffectedStatusSelf(companyId, userId, BagToolTypeEnum.CUPID.getValue());
            List<BagToolEffectEntity> otherCupid = bagToolEffectMapper.getUserEffectedStatusOther(companyId, userId, BagToolTypeEnum.CUPID.getValue());
            List<BagToolEffectEntity> selfDecay = bagToolEffectMapper.getUserEffectedStatusOther(companyId, userId, BagToolTypeEnum.DECAY.getValue());
            List<BagToolEffectEntity> selfSeal = bagToolEffectMapper.getUserEffectedStatusOther(companyId, userId, BagToolTypeEnum.SEAL.getValue());
            if (CollectionUtils.isNotEmpty(selfCupid) || CollectionUtils.isNotEmpty(otherCupid) ||
                    CollectionUtils.isNotEmpty(selfDecay) || CollectionUtils.isNotEmpty(selfSeal)) {
                Integer cupidUserId = null;
                Integer cupidEffectUserId = null;
                Integer cupidEffectId = null;
                //丘比特双方
                //自己发起
                if (CollectionUtils.isNotEmpty(selfCupid)) {
                    cupidUserId = selfCupid.get(0).getUserId(); //自己
                    cupidEffectUserId = selfCupid.get(0).getEffectUserId(); //别人
                    cupidEffectId = selfCupid.get(0).getId();
                    //被别人连
                } else if (CollectionUtils.isNotEmpty(otherCupid)) {
                    cupidUserId = otherCupid.get(0).getEffectUserId(); //自己
                    cupidEffectUserId = otherCupid.get(0).getUserId();  //别人
                    cupidEffectId = otherCupid.get(0).getId();
                }
                //没有衰神
                if (CollectionUtils.isEmpty(selfDecay)) {
                    //有封印
                    if (CollectionUtils.isNotEmpty(selfSeal)) {
                        BagToolEffectEntity bagToolEffectEntity = selfSeal.get(0);
                        if (effectTooId != null && effectTooId.equals(BagToolTypeEnum.SEAL.getValue())) {
                            bagToolEffectEntity = bagToolEffectMapper.getById(effectId);
                        }
                        BagToolEffectQuestionEntity bagToolEffectQuestionEntity = new BagToolEffectQuestionEntity(companyId, BagToolTypeEnum.SEAL.getValue(), userId, userXQuestionsEntity.getId(), bagToolEffectEntity.getId(), new Date());
                        bagToolEffectQuestionsMapper.createOne(bagToolEffectQuestionEntity);
                        Integer effectCount = bagToolEffectQuestionsMapper.getToolEffectCount(companyId, userId, bagToolEffectEntity.getId());
                        //解除封印
                        if (effectCount >= SEAL_COUNT) {
                            bagToolEffectMapper.dispelTool(bagToolEffectEntity.getId(), StatusEnum.DELETE.getValue());
                        }
                    }
                    //插入用户答题记录,bag_tool_effect_question丘比特记录
                    if (CollectionUtils.isNotEmpty(selfCupid) || CollectionUtils.isNotEmpty(otherCupid)) {
                        UserXQuestionsEntity userXQuestionsToolEntity = new UserXQuestionsEntity(companyId, cupidUserId, userXQuestionsEntity.getQuestionId(), 0, StatusEnum.OK.getValue(), null, BagToolTypeEnum.SEAL.getValue(), StatusEnum.TOOL.getValue(), new Date(), new Date());
                        userXQuestionsMapper.createOrUpdateUserXQuestion(userXQuestionsToolEntity);
                        BagToolEffectQuestionEntity bagToolEffectQuestionEntity = new BagToolEffectQuestionEntity(companyId, BagToolTypeEnum.CUPID.getValue(), cupidEffectUserId, userXQuestionsToolEntity.getId(), cupidEffectId, new Date());
                        bagToolEffectQuestionEntity.setAddExp(1);
                        bagToolEffectQuestionsMapper.createOne(bagToolEffectQuestionEntity);
                        bagToolEffectQuestionsMapper.updateOne(new BagToolEffectQuestionEntity(bagToolEffectQuestionEntity.getId(), 1));
                        //双方获取猫粮+10
                        PetFoodEntity petFoodEntity = petFoodMapper.getPetFood(companyId, cupidUserId);
                        if (petFoodEntity == null) {
                            petFoodEntity = new PetFoodEntity(companyId, cupidUserId, EVERY_GAIN_COUNT, new Date(), new Date());
                        }
                        PetFoodEntity petFoodEntity1 = petFoodMapper.getPetFood(companyId, cupidEffectUserId);
                        if (petFoodEntity1 == null) {
                            petFoodEntity1 = new PetFoodEntity(companyId, cupidEffectUserId, EVERY_GAIN_COUNT, new Date(), new Date());
                        }
//                        MyPetVO loverPetVO = myPetMapper.getMyPet(cupidUserId);
                        updatePetFood(petFoodEntity1, PetFoodGainEnum.CUPIDANSWER.getValue());
                        return updatePetFood(petFoodEntity, PetFoodGainEnum.ANSWER.getValue());
                    } else {
                        PetFoodEntity petFoodEntity = petFoodMapper.getPetFood(companyId, userId);
                        if (petFoodEntity == null) {
                            petFoodEntity = new PetFoodEntity(companyId, userId, EVERY_GAIN_COUNT, new Date(), new Date());
                        }
                        return updatePetFood(petFoodEntity, PetFoodGainEnum.ANSWER.getValue());
                    }
                    //有衰神
                } else {
                    //无法获猫粮
                    BagToolEffectEntity bagToolEffectEntity = selfDecay.get(0);
                    if (effectTooId != null && effectTooId.equals(BagToolTypeEnum.DECAY.getValue())) {
                        bagToolEffectEntity = bagToolEffectMapper.getById(effectId);
                    }
                    BagToolEffectQuestionEntity bagToolEffectQuestionEntity = new BagToolEffectQuestionEntity(companyId, BagToolTypeEnum.DECAY.getValue(), userId, userXQuestionsEntity.getId(), bagToolEffectEntity.getId(), new Date());
                    bagToolEffectQuestionsMapper.createOne(bagToolEffectQuestionEntity);
                    Integer effectCount = bagToolEffectQuestionsMapper.getToolEffectCount(companyId, userId, bagToolEffectEntity.getId());
                    //解除封印
                    if (effectCount >= DECAY_COUNT) {
                        bagToolEffectMapper.dispelTool(bagToolEffectEntity.getId(), StatusEnum.DELETE.getValue());
                    }

                    //有小衰神同时有封印咒
                    if (CollectionUtils.isNotEmpty(selfSeal)) {
                        BagToolEffectEntity bagToolEffectSealEntity = selfSeal.get(0);
                        if (effectTooId != null && effectTooId.equals(BagToolTypeEnum.SEAL.getValue())) {
                            bagToolEffectSealEntity = bagToolEffectMapper.getById(effectId);
                        }
                        BagToolEffectQuestionEntity bagToolEffectSealQuestionEntity = new BagToolEffectQuestionEntity(companyId, BagToolTypeEnum.SEAL.getValue(), userId, userXQuestionsEntity.getId(), bagToolEffectSealEntity.getId(), new Date());
                        bagToolEffectQuestionsMapper.createOne(bagToolEffectSealQuestionEntity);
                        Integer effectSealCount = bagToolEffectQuestionsMapper.getToolEffectCount(companyId, userId, bagToolEffectSealEntity.getId());
                        //解除封印
                        if (effectSealCount >= SEAL_COUNT) {
                            bagToolEffectMapper.dispelTool(bagToolEffectSealEntity.getId(), StatusEnum.DELETE.getValue());
                        }
                    }
                    return 2;
                }
            } else {
                PetFoodEntity petFoodEntity = petFoodMapper.getPetFood(companyId, userId);
                if (petFoodEntity == null) {
                    petFoodEntity = new PetFoodEntity(companyId, userId, EVERY_GAIN_COUNT, new Date(), new Date());
                }
                return updatePetFood(petFoodEntity, PetFoodGainEnum.ANSWER.getValue());
            }
        } else {
            return 0;
        }
    }

    /**
     * @Param gainType获取食物方式
     */
    private Integer updatePetFood(PetFoodEntity petFoodEntity, Integer gainType) {
        Integer isSuccess = 0;
        if (petFoodEntity.getId() == null) {
            Integer resultFood = petFoodMapper.createPetFood(petFoodEntity);
            Integer resultFoodDetail = petFoodDetailMapper.createPetFoodDetail(new PetFoodDetailEntity(petFoodEntity.getCompanyId(), petFoodEntity.getUserId(), gainType, EVERY_GAIN_COUNT, new Date(), new Date()));
            if (resultFood > 0 && resultFoodDetail > 0) {
                isSuccess = 1;
            }
            return isSuccess;
        } else {
            Integer petFood = petFoodEntity.getFoodCount();//当前剩余猫粮
            petFood += EVERY_GAIN_COUNT;
            petFoodEntity.setFoodCount(petFood);
            Integer resultFood = petFoodDetailMapper.createPetFoodDetail(new PetFoodDetailEntity(petFoodEntity.getCompanyId(), petFoodEntity.getUserId(), gainType, EVERY_GAIN_COUNT, new Date(), new Date()));
            Integer resultFoodDetail = petFoodMapper.updatePetFood(petFoodEntity);
            if (resultFood > 0 && resultFoodDetail > 0) {
                isSuccess = 1;
            }
            return isSuccess;
        }
    }


    /**
     * 更新宠物经验
     */
    private Integer updatePetExperience(Integer userId, Integer exp) {
        MyPetVO myPetVO = myPetMapper.getMyPet(userId);
        if (myPetVO != null) {
            Integer level = myPetVO.getLevel();
            if (myPetMapper.updatePetExp(new MyPetEntity(myPetVO.getId(), exp)) > 0) {
                myPetVO = myPetMapper.getMyPet(userId);
                PetExperienceEntity petExperienceEntity = petExperienceMapper.getByExperience(myPetVO.getExperienceValue());
                if (petExperienceEntity != null && petExperienceEntity.getLevel() > level) {
                    Integer physicalValue = myPetVO.getPhysicalValue() + petExperienceEntity.getPhysicalValue();
                    physicalValue = physicalValue > petExperienceEntity.getMaxPhysicalValue() ? petExperienceEntity.getMaxPhysicalValue() : physicalValue;
                    myPetMapper.updatePetPhysicalValue(myPetVO.getId(), physicalValue, petExperienceEntity.getLevel());
                    return 2;
                } else {
                    return 1;
                }
            }
            return 0;
        } else {
            return 0;
        }
    }


    /**
     * P1.按照体重来排,其他的随机排
     * public JSONObject getWeekAnswerRankList(PageEntity pageEntity) {
     * MobileLoginUser mobileLoginUser = MobileLoginUser.getUser();
     * Integer companyId = mobileLoginUser.getCompanyID();
     * Integer userId = mobileLoginUser.getUserID();
     * String corpId = mobileLoginUser.getCorpID();
     * //根据当前宠物体重进行排序
     * //获取有宠物重量的用户
     * List<Integer> petWeightUserList = petWeightMapper.getPetWeightUserList(corpId);
     * List<Integer> remainUserList = userXCompanyMapper.getRemainUserIdList2(corpId, petWeightUserList);
     * PageInfo<UserPetWeightRankVO> pageInfoList = null;
     * PageHelper.startPage(pageEntity.getPageNo(), pageEntity.getPageSize());
     * if (CollectionUtils.isNotEmpty(petWeightUserList)) {
     * pageInfoList = new PageInfo<>(petWeightMapper.getRankWeightList(companyId, petWeightUserList, remainUserList));
     * } else {
     * pageInfoList = new PageInfo<>(petWeightMapper.getRankOtherList(companyId, remainUserList));
     * }
     * List<UserPetWeightRankVO> userAnswerRankVOList = pageInfoList.getList();
     * Integer rank = 0;
     * for (int i = 0; i < userAnswerRankVOList.size(); i++) {
     * rank = (pageEntity.getPageNo() - 1) * pageEntity.getPageSize() + i + 1;
     * UserPetWeightRankVO userPetWeightRankVO = userAnswerRankVOList.get(i);
     * Integer rankUserId = userPetWeightRankVO.getUserId();
     * UserEntity userEntity = userMapp.getUserById(rankUserId);
     * Integer departmentId = userXDeptService.getDepartmentId(corpId, rankUserId);
     * String deptname = departmentMapper.getDeptNameById(companyId, departmentId);
     * //            String userTitle = userXTitleService.findUseWearTitleByCompany(companyId, userAnswerRankVO.getUserId());
     * UserXTitleVO userTitle = userXTitleService.findUserWearTitleByCompany(companyId, rankUserId);
     * Integer existPaperBallCount = paperBallMapper.getExistPaperBallCount(companyId, rankUserId);
     * userPetWeightRankVO.setName(userEntity.getName() != null ? userEntity.getName() : "");
     * userPetWeightRankVO.setHeadImage(userEntity.getAvatar() != null ? userEntity.getAvatar() : "");
     * userPetWeightRankVO.setRank(rank);
     * userPetWeightRankVO.setDeptName(deptname);
     * MyPetVO myPetVO = myPetMapper.getMyPet(rankUserId);
     * if (userTitle != null) {
     * userPetWeightRankVO.setTitle(userTitle.getTitle());
     * userPetWeightRankVO.setTitleType(userTitle.getType());
     * }
     * if (existPaperBallCount > 0) {
     * userPetWeightRankVO.setPaperBall(1);
     * } else {
     * userPetWeightRankVO.setPaperBall(0);
     * }
     * if (myPetVO != null) {
     * userPetWeightRankVO.setIsNewUser(0);
     * } else {
     * userPetWeightRankVO.setIsNewUser(1);
     * }
     * <p/>
     * }
     * <p/>
     * <p/>
     * UserPetWeightRankVO myRankVO = new UserPetWeightRankVO();
     * List<UserPetWeightRankVO> userRankList = null;
     * if (CollectionUtils.isNotEmpty(petWeightUserList)) {
     * userRankList = petWeightMapper.getRankWeightList(companyId, petWeightUserList, remainUserList);
     * } else {
     * userRankList = petWeightMapper.getRankOtherList(companyId, remainUserList);
     * }
     * UserEntity userEntity = userMapp.getUserById(userId);
     * for (int i = 0; i < userRankList.size(); i++) {
     * UserPetWeightRankVO userAnswerRankVO = userRankList.get(i);
     * Integer userAnserUserId = userAnswerRankVO.getUserId();
     * if (userAnserUserId.equals(userId)) {
     * Integer departmentId = userXDeptService.getDepartmentId(corpId, userAnswerRankVO.getUserId());
     * String deptname = departmentMapper.getDeptNameById(companyId, departmentId);
     * UserXTitleVO userTitle = userXTitleService.findUserWearTitleByCompany(companyId, userAnswerRankVO.getUserId());
     * myRankVO = new UserPetWeightRankVO(userAnserUserId, userEntity.getName(), userEntity.getAvatar(), i + 1, userAnswerRankVO.getWeight());
     * myRankVO.setDeptName(deptname);
     * if (userTitle != null) {
     * myRankVO.setTitle(userTitle.getTitle());
     * myRankVO.setTitleType(userTitle.getType());
     * }
     * Integer existPaperBallCount = paperBallMapper.getExistPaperBallCount(companyId, userAnserUserId);
     * if (existPaperBallCount > 0) {
     * myRankVO.setPaperBall(1);
     * } else {
     * myRankVO.setPaperBall(0);
     * }
     * break;
     * }
     * }
     * <p/>
     * Map<String, Object> dataMap = Maps.newHashMap();
     * dataMap.put("list", userAnswerRankVOList);
     * dataMap.put("total", pageInfoList.getTotal());
     * dataMap.put("page", new Page(pageInfoList.getPrePage(), pageEntity.getPageNo(), pageInfoList.getNextPage(), pageInfoList.getLastPage()));
     * dataMap.put("myrank", myRankVO);
     * return ResultJson.succResultJson(dataMap);
     * }
     */

    public JSONObject getWeekAnswerRankList(PageEntity pageEntity) {
        Long startTime = System.currentTimeMillis();
        MobileLoginUser mobileLoginUser = MobileLoginUser.getUser();
        Integer companyId = mobileLoginUser.getCompanyID();
        Integer userId = mobileLoginUser.getUserID();
        String corpId = mobileLoginUser.getCorpID();
        //获取答过题的用户
        List<Integer> weekUserAnswerList = userXQuestionsMapper.getAnsweredUserList2(companyId,corpId);
        List<Integer> petUserList = myPetMapper.getUsersByCompany(corpId, weekUserAnswerList);
        //获取剩余的用户
        List<Integer> remainUserList = userXCompanyMapper.getRemainUserIdList3(corpId, weekUserAnswerList, petUserList);

        logger.info("answer:" + weekUserAnswerList.size() +"pet:"+ petUserList.size() +"remain:" + remainUserList.size());
        //UserPetWeightRankVO
        PageHelper.startPage(pageEntity.getPageNo(), pageEntity.getPageSize());
        PageInfo<UserPetWeightRankVO> pageInfo = new PageInfo<>(userXQuestionsMapper.getWeekAnswerRankList3(companyId, corpId, petUserList,remainUserList));
        List<UserPetWeightRankVO> userAnswerRankVOList = pageInfo.getList();
        Integer rank = 0;
        for (int i = 0; i < userAnswerRankVOList.size(); i++) {
            rank = (pageEntity.getPageNo() - 1) * pageEntity.getPageSize() + i + 1;
            UserPetWeightRankVO userAnswerRankVO = userAnswerRankVOList.get(i);
            Integer rankUserId = userAnswerRankVO.getUserId();
            UserEntity userEntity = userMapp.getUserById(rankUserId);
            UserXTitleVO userTitle = userXTitleService.findUserWearTitleByCompany(companyId, rankUserId);
            userAnswerRankVO.setName(userEntity.getName() != null ? userEntity.getName() : "");
            userAnswerRankVO.setHeadImage(userEntity.getAvatar() != null ? userEntity.getAvatar() : "");
            userAnswerRankVO.setRank(rank);
            if (userTitle != null) {
                userAnswerRankVO.setTitle(userTitle.getTitle());
                userAnswerRankVO.setTitleType(userTitle.getType());
            }
            Integer existPaperBallCount = paperBallMapper.getExistPaperBallCount(companyId, rankUserId);
            MyPetVO myPetVO = myPetMapper.getMyPet(rankUserId);
            if (existPaperBallCount > 0) {
                userAnswerRankVO.setPaperBall(1);
            } else {
                userAnswerRankVO.setPaperBall(0);
            }
            if (myPetVO != null) {
                userAnswerRankVO.setIsNewUser(0);
            } else {
                userAnswerRankVO.setIsNewUser(1);
            }
        }

        Long endTime1 = System.currentTimeMillis();
        logger.info("timeCost1:"+(endTime1-startTime));


        //获取我的排行
        UserPetWeightRankVO myRankVO = new UserPetWeightRankVO();
        List<UserPetWeightRankVO> userAnswerList = userXQuestionsMapper.getWeekAnswerRankList3(companyId, corpId, petUserList, remainUserList);
        UserEntity userEntity = userMapp.getUserById(mobileLoginUser.getUserID());
        for (int i = 0; i < userAnswerList.size(); i++) {
            UserPetWeightRankVO userAnswerRankVO = userAnswerList.get(i);
            Integer userAnserUserId = userAnswerRankVO.getUserId();
            if (userAnserUserId.equals(userId)) {
                UserXTitleVO userTitle = userXTitleService.findUserWearTitleByCompany(companyId, userId);
//                String userTitle = userXTitleService.findUseWearTitleByCompany(companyId,userAnserUserId);
                myRankVO = new UserPetWeightRankVO(userAnserUserId, userEntity.getName(), userEntity.getAvatar(), i + 1, userAnswerRankVO.getAnswerCount());
                myRankVO.setCertCount(userAnswerRankVO.getCertCount());
                myRankVO.setTitleCount(userAnswerRankVO.getTitleCount());
                if (userTitle != null) {
                    myRankVO.setTitle(userTitle.getTitle());
                    myRankVO.setTitleType(userTitle.getType());
                }
                Integer existPaperBallCount = paperBallMapper.getExistPaperBallCount(companyId, userId);
                MyPetVO myPetVO = myPetMapper.getMyPet(userId);
                if (existPaperBallCount > 0) {
                    myRankVO.setPaperBall(1);
                } else {
                    myRankVO.setPaperBall(0);
                }
                if (myPetVO != null) {
                    myRankVO.setIsNewUser(0);
                } else {
                    myRankVO.setIsNewUser(1);
                }
                break;
            }
        }
        Map<String, Object> dataMap = Maps.newHashMap();
        dataMap.put("list", userAnswerRankVOList);
        dataMap.put("total", pageInfo.getTotal());
        dataMap.put("myrank", myRankVO);
        dataMap.put("page", new Page(pageInfo.getPrePage(), pageEntity.getPageNo(), pageInfo.getNextPage(), pageInfo.getLastPage()));
        Long endTime2 = System.currentTimeMillis();
        logger.info("timeCost2:"+(endTime2-startTime));
        return ResultJson.succResultJson(dataMap);
    }

    @Override
    public JSONObject getStudyRank(Integer status,String search,PageEntity pageEntity) {
        String userStatus = UserStatusEnum.getUserStatusEnum(status).getChiDesc();
        LoginUser loginUser = LoginUser.getUser();
        Integer companyId = loginUser.getCompanyID();
        String corpId = loginUser.getCorpID();
        PageHelper.startPage(pageEntity.getPageNo(), pageEntity.getPageSize());
        PageInfo<UserRankVO> pageInfo = new PageInfo<>(userXQuestionsMapper.getStudyRank(companyId,status,search));
        Integer indexStart = (pageEntity.getPageNo()-1) * pageEntity.getPageSize();
        List<UserRankVO> resultList = pageInfo.getList();
        for (int i = 0; i < resultList.size(); i++) {
            Integer rank = indexStart+i+1;
            UserRankVO userRankVO = resultList.get(i);
            userRankVO.setStatus(userStatus);
            Integer departmentId = userXDeptService.getDepartmentId(corpId, userRankVO.getUserId());
            String departmentName = departmentMapper.getDeptNameById(companyId, departmentId);
            String dingName = userMapp.getNameById(userRankVO.getUserId());
            userRankVO.setDingName(dingName);
            userRankVO.setDepartment(departmentName);
            userRankVO.setRank(rank);
        }
        Map<String, Object> dataMap = Maps.newHashMap();
        dataMap.put("list", resultList);
        dataMap.put("total", pageInfo.getTotal());
        dataMap.put("page", new Page(pageInfo.getPrePage(), pageEntity.getPageNo(), pageInfo.getNextPage(), pageInfo.getLastPage()));
        return ResultJson.succResultJson(dataMap);
    }

    /**
     * 统=同一题答题算多次 没有做排除
     * */
    @Override
    public JSONObject getStudyRankDetail(Integer type,Integer userId) {
        //同一题答多次算多次
        LoginUser loginUser = LoginUser.getUser();
        Integer companyId = loginUser.getCompanyID();
        List<UserRankDetailVO> resultList = new ArrayList<>();
        List<UserRankEnterpriseDetailVO> resultList2 = new ArrayList<>();
        List<Integer> libraryIdList = userXQuestionsMapper.getUserLibraryList(loginUser.getCompanyID(), userId, type);
        //官方题库
        if(type==2) {
            for (int i = 0; i < libraryIdList.size(); i++) {
                Integer libraryId = libraryIdList.get(i);
                String libraryName = questionLibraryMapper.getLibraryNameById(libraryId);
                Integer studyCount = userXQuestionsMapper.getStudyCountByLibraryId(companyId, libraryId, userId);
                Integer rightStudyCount = userXQuestionsMapper.getRightStudyCountByLibraryId(companyId, libraryId, userId);
//                Integer rightStudyCount = userXQuestionsMapper.getUserCorrectQuestionsByLibraryId(companyId, libraryId, userId);
                String title = "";
                QuestionLibraryTitleEntity questionLibraryTitleEntity = questionLibraryTitleMapper.getQuestionTitleByLibrary(libraryId);
                if(questionLibraryTitleEntity==null){
                    title = NO_TITLE;
                }else{
                    title = userXTitleMapper.findUserTitleByLibraryId(companyId, userId, libraryId);
                    if(title==null || StringUtils.isEmpty(title)){
                        title = DEFAULT_TITLE;
                    }
                }
                UserRankDetailVO userRankDetailVO = new UserRankDetailVO(userId, libraryName, studyCount, rightStudyCount, title);
                resultList.add(userRankDetailVO);
            }
            return ResultJson.succResultJson(resultList);
        }else{
            for (int i = 0; i < libraryIdList.size(); i++) {
                Integer libraryId = libraryIdList.get(i);
                String libraryName = questionLibraryMapper.getLibraryNameById(libraryId);
                Integer totalCount = questionsMapper.getTotalCountByLibraryId(libraryId);
                Integer answerCount = userXLibraryMapper.getCorrectCount(companyId, userId, libraryId);
                String accuracy = QuestionUtil.getPercentDivision(answerCount, totalCount);
                Integer correctAnsweredQuestionNumber = userXQuestionsMapper.getUserCorrectQuestionsByLibraryId(companyId, userId, libraryId);
                Integer totoalCountInLibrary = questionsMapper.getTotalCountByLibraryId(libraryId);
                String schedule = QuestionUtil.getPercentDivision(correctAnsweredQuestionNumber, totoalCountInLibrary);
                //Integer userId, String libraryName, Integer studyCount, String schedule, String accuracy
                Integer rightStudyCount = userXQuestionsMapper.getRightStudyCountByLibraryId(companyId, libraryId, userId);
                UserRankEnterpriseDetailVO userRankEnterpriseDetail = new UserRankEnterpriseDetailVO(userId,libraryName,rightStudyCount,schedule,accuracy);
                resultList2.add(userRankEnterpriseDetail);
            }
            return ResultJson.succResultJson(resultList2);
        }
    }

    /**
     * 题库热度排行
     * */
    @Override
    public JSONObject getLibraryHeatRank(Integer type, String search,PageEntity pageEntity) {
        LoginUser loginUser = LoginUser.getUser();
        Integer companyId = loginUser.getCompanyID();
        Integer indexStart = (pageEntity.getPageNo()-1) * pageEntity.getPageSize();
        PageHelper.startPage(pageEntity.getPageNo(), pageEntity.getPageSize());
        PageInfo<QuestionLibraryHeatRank> pageInfo = new PageInfo<>(userXQuestionsMapper.getQuestionLibraryByStudyCount(companyId,type,search));
        for (int i = 0; i < pageInfo.getList().size(); i++) {
            Integer rank = indexStart+i+1;
            QuestionLibraryHeatRank questionLibraryHeat = pageInfo.getList().get(i);
            questionLibraryHeat.setRank(rank);
        }
        Map<String, Object> dataMap = Maps.newHashMap();
        dataMap.put("list", pageInfo.getList());
        dataMap.put("total", pageInfo.getTotal());
        dataMap.put("page", new Page(pageInfo.getPrePage(), pageEntity.getPageNo(), pageInfo.getNextPage(), pageInfo.getLastPage()));
        return ResultJson.succResultJson(dataMap);
    }

    /**
     * 排行榜分享详情
     */
    @Override
    public JSONObject getWeekAnswerRankShare(String corpId, PageEntity pageEntity) {
        Integer companyId = isvTicketsService.getIsvTicketByCorpId(corpId).getCompanyId();
        AssertUtil.notNull(companyId, "无效的companyId");

        List<Integer> petWeightUserList = petWeightMapper.getPetWeightUserList(corpId);
        List<Integer> remainUserList = userXCompanyMapper.getRemainUserIdList2(corpId, petWeightUserList);
        PageInfo<UserPetWeightRankVO> pageInfoList = null;
        PageHelper.startPage(pageEntity.getPageNo(), pageEntity.getPageSize());
        if (CollectionUtils.isNotEmpty(petWeightUserList)) {
            pageInfoList = new PageInfo<>(petWeightMapper.getRankWeightList(companyId, petWeightUserList, remainUserList));
        } else {
            pageInfoList = new PageInfo<>(petWeightMapper.getRankOtherList(companyId, remainUserList));
        }
        List<UserPetWeightRankVO> userAnswerRankVOList = pageInfoList.getList();
        Integer rank = 0;
        for (int i = 0; i < userAnswerRankVOList.size(); i++) {
            rank = (pageEntity.getPageNo() - 1) * pageEntity.getPageSize() + i + 1;
            UserPetWeightRankVO userPetWeightRankVO = userAnswerRankVOList.get(i);
            Integer rankUserId = userPetWeightRankVO.getUserId();
            UserEntity userEntity = userMapp.getUserById(rankUserId);
//            String userTitle = userXTitleService.findUseWearTitleByCompany(companyId, userAnswerRankVO.getUserId());
            UserXTitleVO userTitle = userXTitleService.findUserWearTitleByCompany(companyId, rankUserId);
            Integer existPaperBallCount = paperBallMapper.getExistPaperBallCount(companyId, rankUserId);
            MyPetVO myPetVO = myPetMapper.getMyPet(rankUserId);
            userPetWeightRankVO.setName(userEntity.getName() != null ? userEntity.getName() : "");
            userPetWeightRankVO.setHeadImage(userEntity.getAvatar() != null ? userEntity.getAvatar() : "");
            userPetWeightRankVO.setRank(rank);
            if (userTitle != null) {
                userPetWeightRankVO.setTitle(userTitle.getTitle());
                userPetWeightRankVO.setTitleType(userTitle.getType());
            }
            if (existPaperBallCount > 0) {
                userPetWeightRankVO.setPaperBall(1);
            } else {
                userPetWeightRankVO.setPaperBall(0);
            }
            if (myPetVO != null) {
                userPetWeightRankVO.setIsNewUser(0);
            } else {
                userPetWeightRankVO.setIsNewUser(1);
            }
        }

        Map<String, Object> dataMap = Maps.newHashMap();
        dataMap.put("list", userAnswerRankVOList);
        dataMap.put("total", pageInfoList.getTotal());
        dataMap.put("page", new Page(pageInfoList.getPrePage(), pageEntity.getPageNo(), pageInfoList.getNextPage(), pageInfoList.getLastPage()));
        return ResultJson.succResultJson(dataMap);
    }


    @Override
    public JSONObject getMyRank(String corpId) {
        MobileLoginUser mobileLoginUser = MobileLoginUser.getUser();
            Long startSeconds = System.currentTimeMillis();
        //Integer userID, Integer companyID, Integer role, String corpID, String dingID, String dingName, String avatar
        //获取周开始时间和结束时间
        Integer companyId = isvTicketsService.getIsvTicketByCorpId(corpId).getCompanyId();
        Date startDateTime = new Date();
        Date startTime = DateUtil.getDateStartDateTime(DateUtil.getFirstDayOfWeek(startDateTime));
        Date endTime = DateUtil.getDateEndDateTime(DateUtil.getLastDayOfWeek(startDateTime));
        //获取我的排行
        UserAnswerRankVO myRankVO = new UserAnswerRankVO();
        List<Integer> weekUserAnswerList = userXQuestionsMapper.getAnsweredUserList(companyId, corpId, startTime, endTime);
        //获取未答过题的宠物等级排序,认领过宠物的用户
        List<Integer> petUserList = myPetMapper.getUsersByCompany(corpId, weekUserAnswerList);
        //获取未答题,未领养宠物的用户
        List<Integer> userOpenUserList = userXOpenMapper.getExceludeUserIdList(companyId, weekUserAnswerList, petUserList);

        List<Integer> remainUserList = userXCompanyMapper.getRemainUserIdList(corpId, weekUserAnswerList, petUserList, userOpenUserList);
        List<UserAnswerRankVO> userAnswerList = userXQuestionsMapper.getWeekAnswerRankList2(mobileLoginUser.getCompanyID(), corpId, startTime, endTime, petUserList, userOpenUserList, remainUserList);
        UserEntity userEntity = userMapp.getUserById(mobileLoginUser.getUserID());
        for (int i = 0; i < userAnswerList.size(); i++) {
            UserAnswerRankVO userAnswerRankVO = userAnswerList.get(i);
            Integer userAnserUserId = userAnswerRankVO.getUserId();
            if (userAnserUserId.equals(mobileLoginUser.getUserID())) {
                myRankVO = new UserAnswerRankVO(userAnserUserId, userEntity.getName(), userEntity.getAvatar(), i + 1, userAnswerRankVO.getAnswerCount());
                UserXTitleVO userTitle = userXTitleService.findUserWearTitleByCompany(companyId, userAnswerRankVO.getUserId());
                if (userTitle != null) {
                    myRankVO.setTitle(userTitle.getTitle());
                    myRankVO.setTitleType(userTitle.getType());
                }
                List<BagToolEffectEntity> mosaicEffectedList = bagToolEffectMapper.getUserEffectedStatusOther(companyId, userAnserUserId, BagToolTypeEnum.MOSAIC.getValue());
                if (CollectionUtils.isNotEmpty(mosaicEffectedList)) {
                    myRankVO.setIsMosaic(1);
                } else {
                    myRankVO.setIsMosaic(0);
                }
            }
        }
        Map<String, Object> dataMap = Maps.newHashMap();
        dataMap.put("myrank", myRankVO);
        Long endSeconds = System.currentTimeMillis();
        dataMap.put("timecost", (endSeconds - startSeconds));
        return ResultJson.succResultJson(dataMap);
    }


    public static void main(String[] args) {
//        QuestionAnswerVO questionAnswerVO = new QuestionAnswerVO(1,"C",1,"答题正确");
//        QuestionAnswerVO questionAnswerVO = new QuestionAnswerVO(1, "C", 1, "答题正确");
//        ExperienceVO experienceVO = new ExperienceVO(1, 1, 1, 2, null);
//        Map<String, Object> resultMap = new HashMap<String, Object>();
//        resultMap.put("answer", questionAnswerVO);
//        resultMap.put("exp", experienceVO);
//        JSONObject jsonObject = ResultJson.succResultJson(resultMap);
//        System.out.println(jsonObject.toJSONString());
//        UserAnswerRankVO userAnswerRankVO = new UserAnswerRankVO(1, "十三", "https://static.dingtalk.com/media/lADOxqIK2s0C7s0C7g_750_750.jpg", 1, 20, "开发部");
//        UserAnswerRankVO userAnswerRankVO1 = new UserAnswerRankVO(2, "金瑶", "https://static.dingtalk.com/media/lADPBbCc1T7ewvrNAuDNAuw_748_736.jpg", 2, 10, "设计部");
//        UserAnswerRankVO userAnswerRankVO2 = new UserAnswerRankVO(3, "朱少山", "https://static.dingtalk.com/media/lADPBbCc1TfYJFjNAu7NAug_744_750.jpg", 3, 8, "开发部");
//        List<UserAnswerRankVO> userAnswerRankVOList = new ArrayList<>();
//        userAnswerRankVOList.add(userAnswerRankVO);
//        userAnswerRankVOList.add(userAnswerRankVO1);
//        userAnswerRankVOList.add(userAnswerRankVO2);
//        Map<String, Object> dataMap = Maps.newHashMap();
//        dataMap.put("list", userAnswerRankVOList);
//        dataMap.put("total", 3);
//        dataMap.put("page", new Page(0, 1, null, 1));
//        System.out.println(ResultJson.succResultJson(dataMap).toJSONString());

        //Integer userId, String name, String headImage, Integer rank, Integer answerCount, String deptName
//        List<UserAnswerRankVO> userAnswerRankVOList = new ArrayList<>();
//        userAnswerRankVOList.add(new UserAnswerRankVO(1, "夫差", "http://5.jpg", 1, 10, "产品部"));
//
//        Map<String, Object> dataMap = Maps.newHashMap();
//        dataMap.put("list", userAnswerRankVOList);
//        dataMap.put("total", 2);
//        dataMap.put("myrank", 3);
//        dataMap.put("page", new Page(1, 5, 2, 2));
//        String result = JSON.toJSONString(ResultJson.succResultJson(dataMap), SerializerFeature.WriteMapNullValue);
//        JSONObject jsonObject = JSONObject.parseObject(result);
//        System.out.println(jsonObject.toJSONString());

//        QuestionAnswerDto2 questionAnswerDto2 = new QuestionAnswerDto2(1, 14, 21);
//        QuestionAnswerVO questionAnswerVO = new QuestionAnswerVO(210, "A", 1, "回答正确", "这题很简单", 20, 18);
//        UserXTitleSimpleVO userXTitleSimpleVO = new UserXTitleSimpleVO();
//        BagDropVO dropBoxVO = new BagDropVO(0, 0);
//        userXTitleSimpleVO.setTitle("答题小能手");
//        userXTitleSimpleVO.setAvatar("http://qqq/2.jpg");
//        userXTitleSimpleVO.setTitleType(2);
//        dropBoxVO = new BagDropVO(BagToolGainTypeEnum.CORRECT_TWELVE.getValue(), StatusEnum.OK.getValue());
//        Map<String, Object> resultMap = new HashMap<>();
//        resultMap.put("answer", questionAnswerVO);
//        resultMap.put("petFood", 1);
//        resultMap.put("title", userXTitleSimpleVO);
//        resultMap.put("dropBox", dropBoxVO);
//        String result = ResultJson.succResultJson(resultMap).toJSONString();
//        System.out.println(result);

//        Integer correctAnsweredQuestionNumber = 50;
//        Integer totoalCountInLibrary = 50;
//        DecimalFormat format = new DecimalFormat("0");
//        Integer schedule = schedule =  QuestionUtil.getSchedule(correctAnsweredQuestionNumber, totoalCountInLibrary);
        BigDecimal bigDecimal = new BigDecimal(100);
        if (bigDecimal.compareTo(new BigDecimal(100)) == 0) {
            System.out.println(1);
        } else {
            System.out.println(200);
        }
    }

}
