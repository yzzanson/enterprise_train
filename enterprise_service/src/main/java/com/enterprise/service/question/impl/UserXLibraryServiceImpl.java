package com.enterprise.service.question.impl;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.*;
import com.enterprise.base.entity.QuestionLibraryTitleEntity;
import com.enterprise.base.entity.UserXLibraryEntity;
import com.enterprise.base.entity.UserXQuestionsEntity;
import com.enterprise.base.enums.StatusEnum;
import com.enterprise.base.exceptions.BusinessException;
import com.enterprise.base.vo.UserXLibraryVO;
import com.enterprise.base.vo.UserXLibraryVO2;
import com.enterprise.base.vo.UserXTitleVO;
import com.enterprise.mapper.department.DepartmentMapper;
import com.enterprise.mapper.questions.QuestionLibraryTitleMapper;
import com.enterprise.mapper.questions.QuestionsMapper;
import com.enterprise.mapper.questions.UserXQuestionsMapper;
import com.enterprise.mapper.users.UserXLibraryMapper;
import com.enterprise.mapper.users.UserXTitleMapper;
import com.enterprise.service.isvTickets.IsvTicketsService;
import com.enterprise.service.question.UserXLibraryService;
import com.enterprise.service.user.UserXDeptService;
import com.enterprise.util.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.RateLimiter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * CompanyInfoServiceImpl
 *
 * @author shisan
 * @create 2018-03-26 上午11:00
 **/
@Service
public class UserXLibraryServiceImpl implements UserXLibraryService {

    @Resource
    private UserXLibraryMapper userXLibraryMapper;

    @Resource
    private IsvTicketsService isvTicketsService;

    @Resource
    private UserXDeptService userXDeptService;

    @Resource
    private DepartmentMapper departmentMapper;

    @Resource
    private QuestionsMapper questionsMapper;

    @Resource
    private UserXQuestionsMapper userXQuestionsMapper;

    @Resource
    private UserXTitleMapper userXTitleMapper;

    @Resource
    private QuestionLibraryTitleMapper questionLibraryTitleMapper;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static Integer MAIN_QUESTION_LIBRARY_ID = 61;

    private static Integer EVERY_QUESTION_FOOD = PetConstant.EVERY_GAIN_COUNT;

    private static String DING_LIBRARY_LABEL = "钉钉";

    //4秒允许调用一次
    RateLimiter rateLimiter = RateLimiter.create(0.25, 1, TimeUnit.SECONDS);

    /**
     * 1热门
     * 2企业
     * 3成就
     * 4钉钉题库
     * */
    @Override
    public JSONObject getUserStudyProcess(Integer userId, Integer companyId, Integer type) throws BusinessException {
        List<UserXLibraryVO2> userStudyProcessList = new ArrayList<>();
        if (type.equals(1) || type.equals(2) || type.equals(4)) {
            userStudyProcessList = userXLibraryMapper.getCommonUserLibrary(companyId, userId, type);
        } else {
            //显示所有的题库
            userStudyProcessList = userXLibraryMapper.getAchieveUserLibrary(companyId, userId);
        }
        List<UserXLibraryVO2> resultList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(userStudyProcessList)) {
            for (int i = 0; i < userStudyProcessList.size(); i++) {
                UserXLibraryVO2 userXLibraryVO = userStudyProcessList.get(i);
                Integer libraryId = userXLibraryVO.getLibraryId();
                Integer answerCount = userXQuestionsMapper.getTotalCorrectCountByLibraryId(companyId, libraryId, userId);
                Integer totalCount =  questionsMapper.getTotalCountByLibraryId(libraryId);
                Integer remainFoodCount = (totalCount - answerCount) * EVERY_QUESTION_FOOD;
                userXLibraryVO.setAnsweredCount(answerCount);
                userXLibraryVO.setTotalCount(totalCount);
                userXLibraryVO.setRemainFoodCount(remainFoodCount);

                QuestionLibraryTitleEntity questionLibraryTitleInDB = questionLibraryTitleMapper.getQuestionTitleByLibrary(libraryId);
                if ((type.equals(1) || type.equals(2) || type.equals(4)) && (totalCount.equals(answerCount) && totalCount>0)) {
//                    userStudyProcessList.remove(i);
                    continue;
                }
//                if (type.equals(3) && questionLibraryTitleInDB == null) {
//                    userStudyProcessList.remove(i);
//                }

                if (questionLibraryTitleInDB != null && StringUtils.isNotEmpty(questionLibraryTitleInDB.getTitle()) && questionLibraryTitleInDB.getStatus().equals(StatusEnum.OK.getValue())) {
                    userXLibraryVO.setTitle(questionLibraryTitleInDB.getTitle());
                    userXLibraryVO.setTitleFlag(1);
                    UserXTitleVO userXTitleVO = userXTitleMapper.findUserXTitleByCompanyAndLibrary(companyId, libraryId, userId, StatusEnum.OK.getValue());
                    if (userXTitleVO != null) {
                        userXLibraryVO.setGetFlag(1);
                        if (userXTitleVO.getChooseFlag() != null && userXTitleVO.getChooseFlag().equals(1)) {
                            userXLibraryVO.setChooseFlag(1);
                        } else {
                            userXLibraryVO.setChooseFlag(0);
                        }
                    } else {
                        userXLibraryVO.setGetFlag(0);
                        userXLibraryVO.setChooseFlag(0);
                    }
                    userXLibraryVO.setTitleType(questionLibraryTitleInDB.getType());
                } else {
                    userXLibraryVO.setTitleFlag(0);
                    userXLibraryVO.setGetFlag(0);
                    userXLibraryVO.setChooseFlag(0);
                }
                resultList.add(userXLibraryVO);
            }
        }
        return ResultJson.succResultJson(resultList);
    }

    @Override
    public JSONObject createOrUpdateUserXLibrary(UserXLibraryEntity userXLibraryEntity) {
        if (userXLibraryEntity.getId() == null || (userXLibraryEntity.getId() != null && userXLibraryEntity.getId().equals(0))) {
            if (userXLibraryEntity.getIsUpdate() == null) {
                userXLibraryEntity.setIsUpdate(0);
            }
            Integer result = userXLibraryMapper.createOrUpdateUserXLibrary(userXLibraryEntity);
            if (result > 0)
                return ResultJson.succResultJson(userXLibraryEntity);
            return ResultJson.errorResultJson("新增失败");
        } else {
            Integer result = userXLibraryMapper.updateUserXLibrary(userXLibraryEntity);
            if (result > 0)
                return ResultJson.succResultJson(userXLibraryEntity);
            return ResultJson.errorResultJson("更新失败");
        }
    }

    @Override
    public JSONObject findUserStudySchedule(Integer libraryId, PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPageNo(), pageEntity.getPageSize());
        PageInfo<UserXLibraryVO> pageInfo = new PageInfo<>(userXLibraryMapper.findUserStudySchedule(libraryId));
        Map<String, Object> dataMap = Maps.newHashMap();
        dataMap.put("list", pageInfo.getList());
        dataMap.put("total", pageInfo.getTotal());
        dataMap.put("page", new Page(pageInfo.getPrePage(), pageEntity.getPageNo(), pageInfo.getNextPage(), pageInfo.getLastPage()));
        return ResultJson.succResultJson(dataMap);
    }

    @Override
    public JSONObject getDingLibraryProgress(Integer companyId, Integer userId) {
        try {
            //用户的钉钉官方题库
            List<UserXLibraryVO2> userStudyProcessList = userXLibraryMapper.getDefaultDingPublicQuestionLibrary(companyId,userId,DING_LIBRARY_LABEL);
            Integer totalQuestionCount = 0;
            Integer answerCount = 0;
            if (CollectionUtils.isNotEmpty(userStudyProcessList)) {
                for (int i = 0; i < userStudyProcessList.size(); i++) {
                    Integer libraryId = userStudyProcessList.get(i).getLibraryId();
                    Integer questionCount = questionsMapper.getTotalCountByLibraryId(libraryId);
                    Integer correctAnsweredQuestionNumber = userXQuestionsMapper.getUserCorrectQuestionsByLibraryId(companyId, userId, libraryId);
                    if(questionCount>0 && correctAnsweredQuestionNumber<questionCount) {
                        totalQuestionCount += questionCount;
                        answerCount += correctAnsweredQuestionNumber;
                    }
                }
            }
            Map<String, Object> dataMap = Maps.newHashMap();
            dataMap.put("totalCount", totalQuestionCount);
            dataMap.put("answerCount", answerCount);
            return ResultJson.succResultJson(dataMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultJson.errorResultJson("出问题了");
    }


    @Override
    public UserXLibraryEntity getByUserIdAndLibraryId(Integer companyId, Integer userId, Integer libraryId) {
        return userXLibraryMapper.getByUserIdAndLibraryId(companyId, userId, libraryId);
    }

    @Override
    public Integer batchInsertUserXLibrary(List<UserXLibraryEntity> userXLibraryList) {
        return userXLibraryMapper.batchInsertuserxlibrary(userXLibraryList);
    }

    @Override
    public List<Integer> getUserByCorpIdAndLibraryId(Integer companyId, String corpId, Integer libraryId) {
        return userXLibraryMapper.getUserByCorpIdAndLibraryId(companyId, corpId, libraryId);
    }

    @Override
    public Integer batchUpdateState(Integer libraryId, Integer state) {
        return userXLibraryMapper.batchUpdateState(libraryId, state);
    }

    /**
     * 不分公司获取学习该题库的人
     */
    @Override
    public List<UserXLibraryEntity> getByLibraryId(Integer libraryId) {
        return userXLibraryMapper.getByLibraryId(libraryId, null);
    }

    @Override
    public JSONObject getUserTotalStudy() {
        MobileLoginUser mobileLoginUser = MobileLoginUser.getUser();
        Integer companyId = mobileLoginUser.getCompanyID();
        Integer userId = mobileLoginUser.getUserID();
        List<UserXLibraryVO2> userXLibraryList = userXLibraryMapper.getCommonUserLibrary(companyId, userId, null);
        Integer answerCount = 0;
        for (int i = 0; i < userXLibraryList.size(); i++) {
            Integer libraryId = userXLibraryList.get(i).getLibraryId();
            Integer answerCountLibrary = userXQuestionsMapper.getUserCorrectQuestionsByLibraryId(companyId, userId, libraryId);
            answerCount += answerCountLibrary;
        }
        return ResultJson.succResultJson(answerCount);
    }

    @Override
    public JSONObject updateFinishTime() {
        try {
            List<UserXLibraryEntity> finishedList = userXLibraryMapper.getFinishedList();
            for (int i = 0; i < finishedList.size(); i++) {
                UserXLibraryEntity userXLibraryEntity = finishedList.get(i);
                Integer companyId = userXLibraryEntity.getCompanyId();
                Integer userId = userXLibraryEntity.getUserId();
                Integer libraryId = userXLibraryEntity.getLibraryId();
                UserXQuestionsEntity userXQuestion = userXQuestionsMapper.getLastAnswerTime(companyId, libraryId, userId);
                if (userXQuestion != null) {
                    userXLibraryEntity.setFinishTime(DateUtil.getDisplayYMDHMS(userXQuestion.getCreateTime()));
                    userXLibraryMapper.updateFinishTime(userXLibraryEntity);
                }
                return ResultJson.succResultJson(finishedList.size());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return ResultJson.errorResultJson("更新失败");
    }

    @Override
    public JSONObject updateFinishTime2() {
        try {
            List<UserXLibraryEntity> finishedList = userXLibraryMapper.getFinishedNoTimeList();
            for (int i = 0; i < finishedList.size(); i++) {
                UserXLibraryEntity userXLibraryEntity = finishedList.get(i);
                Integer companyId = userXLibraryEntity.getCompanyId();
                Integer userId = userXLibraryEntity.getUserId();
                Integer libraryId = userXLibraryEntity.getLibraryId();
                UserXQuestionsEntity userXQuestion = userXQuestionsMapper.getLastAnswerTime(companyId, libraryId, userId);
                if (userXQuestion != null) {
                    userXLibraryEntity.setFinishTime(DateUtil.getDisplayYMDHMS(userXQuestion.getCreateTime()));
                    userXLibraryMapper.updateFinishTime(userXLibraryEntity);
                }
            }
            return ResultJson.succResultJson(finishedList.size());
        }catch(Exception e){
            e.printStackTrace();
        }
        return ResultJson.errorResultJson("更新失败");
    }


//    @Override
//    public List<UserXLibraryEntity> getByLibraryId(Integer libraryId,Integer status) {
//        return userXLibraryMapper.getByLibraryId(libraryId,status);
//    }
}
