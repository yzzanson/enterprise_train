package com.enterprise.thread;

import com.enterprise.base.common.SpringContextHolder;
import com.enterprise.base.entity.QuestionsEntity;
import com.enterprise.base.entity.UserXLibraryEntity;
import com.enterprise.base.entity.UserXQuestionsEntity;
import com.enterprise.base.enums.StatusEnum;
import com.enterprise.mapper.questions.UserXQuestionsMapper;
import com.enterprise.mapper.users.UserXLibraryMapper;
import com.enterprise.oa.OAMessageUtil;
import com.enterprise.service.question.QuestionsService;
import com.enterprise.service.question.UserXQuestionsService;
import com.enterprise.util.QuestionUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * SendMsgThread
 *
 * @author anson
 * @create 2018-03-09 下午3:57
 **/
public class ModifyUserQuestionThread implements Runnable {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Integer companyId;

    private Integer questionId;

    private String questionIds;

    //0重新学习  1新增修改题目
    private Integer state;

    private DecimalFormat format = new DecimalFormat("0");

    public ModifyUserQuestionThread(Integer companyId, Integer questionId, Integer state) {
        this.companyId = companyId;
        this.questionId = questionId;
        this.state = state;
    }

    public ModifyUserQuestionThread(String questionIds, Integer state) {
        this.questionIds = questionIds;
        this.state = state;
    }

    @Override
    public void run() {
        UserXQuestionsService userXQuestionsService = SpringContextHolder.getBean(UserXQuestionsService.class);
//        UserXLibraryService userXLibraryService = SpringContextHolder.getBean(UserXLibraryService.class);
        UserXLibraryMapper userXLibraryMapper = SpringContextHolder.getBean(UserXLibraryMapper.class);
        QuestionsService questionsService = SpringContextHolder.getBean(QuestionsService.class);
        UserXQuestionsMapper userXQuestionMapper = SpringContextHolder.getBean(UserXQuestionsMapper.class);
        List<UserXLibraryEntity> updateUserLibraryList = new ArrayList<>();
        //重学时会有
        List<UserXLibraryEntity> insertLibraryList = new ArrayList<>();
        if (!state.equals(2)) {
            QuestionsEntity questionsEntity = questionsService.getById(questionId);
            Integer libraryId = questionsEntity.getLibraryId();
            //重学题重新计算学习进度
            Integer totalCount = questionsService.getQuestionCount(libraryId);
            //新增题重新计算所有人学习进度
            if (state.equals(0)) {
                List<UserXLibraryEntity> userXLibraryList = userXLibraryMapper.getByLibraryId(libraryId, null);
                for (int i = 0; i < userXLibraryList.size(); i++) {
                    UserXLibraryEntity userXLibraryEntity = userXLibraryList.get(i);
                    Integer companyId = userXLibraryEntity.getCompanyId();
                    Integer correctAnsweredQuestionNumber = userXQuestionMapper.getUserCorrectQuestionsByLibraryId(companyId, userXLibraryEntity.getUserId(), libraryId);
                    BigDecimal schedule = new BigDecimal(QuestionUtil.getPercentDivision(correctAnsweredQuestionNumber, totalCount));
                    userXLibraryEntity.setAnswerCount(correctAnsweredQuestionNumber);
                    userXLibraryEntity.setSchedule(schedule);
                    userXLibraryEntity.setUpdateTime(new Date());
                    userXLibraryEntity.setFinishTime(null);
                    updateUserLibraryList.add(userXLibraryEntity);
                }
                if (CollectionUtils.isNotEmpty(updateUserLibraryList)) {
                    userXLibraryMapper.batchUpdateNew(updateUserLibraryList);
                }
                //重新学习计算回答过题的人的进度
            } else if (state.equals(0)) {
                Integer batchUpdateResult = userXQuestionsService.batchUpdateStatus(questionId, StatusEnum.HOLD_ON.getValue());
                if (batchUpdateResult > 0) {
                    String messageContnt = getMessageContent(questionId, batchUpdateResult);
                    OAMessageUtil.sendTextMsgToDept(messageContnt);
                }
                List<UserXQuestionsEntity> answeredUserList = userXQuestionsService.getCorrectAnswerUserList(null, questionId);
                for (int i = 0; i < answeredUserList.size(); i++) {
                    UserXQuestionsEntity userXQuestionEntity = answeredUserList.get(i);
                    Integer companyId = userXQuestionEntity.getCompanyId();
                    Integer userId = userXQuestionEntity.getUserId();
                    Integer correctAnsweredQuestionNumber = userXQuestionMapper.getUserCorrectQuestionsByLibraryId(companyId, userId, libraryId);
                    BigDecimal schedule = new BigDecimal(QuestionUtil.getPercentDivision(correctAnsweredQuestionNumber, totalCount));
                    UserXLibraryEntity userXLibraryEntity = userXLibraryMapper.getByUserIdAndLibraryId(companyId, userId, libraryId);
                    if (userXLibraryEntity != null) {
                        userXLibraryEntity.setAnswerCount(correctAnsweredQuestionNumber);
                        userXLibraryEntity.setSchedule(schedule);
                        userXLibraryEntity.setUpdateTime(new Date());
                        userXLibraryEntity.setFinishTime(null);
                        updateUserLibraryList.add(userXLibraryEntity);
                    } else {
                        //public UserXLibraryEntity(Integer companyId, Integer userId, Integer libraryId, Integer schedule, Integer answerCount, Date lastAnswerTime, Integer isUpdate, Integer status, Date createTime, Date updateTime) {
                        userXLibraryEntity = new UserXLibraryEntity(companyId, userId, libraryId, schedule, correctAnsweredQuestionNumber, new Date(), null, StatusEnum.OK.getValue(), new Date(), new Date());
                        insertLibraryList.add(userXLibraryEntity);
                    }
                }
                if (CollectionUtils.isNotEmpty(insertLibraryList)) {
                    userXLibraryMapper.batchInsertuserxlibrary(insertLibraryList);
                }
                if (CollectionUtils.isNotEmpty(updateUserLibraryList)) {
                    userXLibraryMapper.batchUpdateNew(updateUserLibraryList);
                }
                //删除题目,对答过该题的人进度进行重新计算
            }
        } else if (state.equals(2)) {
            //获取答过该题的用户
            String[] questionArray = questionIds.split(",");
            List<Integer> questionList = new ArrayList<>();
            for (int i = 0; i < questionArray.length; i++) {
                questionList.add(Integer.valueOf(questionArray[i]));
            }
            Integer libraryId = questionsService.getById(questionList.get(0)).getLibraryId();
            Integer totalCount = questionsService.getQuestionCount(libraryId);
            List<UserXQuestionsEntity> userQuestionList = userXQuestionMapper.getAnsweredUserListGroup(questionList);
            for (int i = 0; i < userQuestionList.size(); i++) {
                UserXQuestionsEntity userXQuestionEntity = userQuestionList.get(i);
                Integer companyId = userXQuestionEntity.getCompanyId();
                Integer userId = userXQuestionEntity.getUserId();
                UserXLibraryEntity userXLibraryEntity = userXLibraryMapper.getByUserIdAndLibraryId(companyId, userId, libraryId);
                if (userXLibraryEntity != null) {
                    Integer correctAnsweredQuestionNumber = userXQuestionMapper.getUserCorrectQuestionsByLibraryId(companyId, userId, libraryId);
                    BigDecimal schedule = new BigDecimal(QuestionUtil.getPercentDivision(correctAnsweredQuestionNumber, totalCount));
                    userXLibraryEntity.setUpdateTime(new Date());
                    userXLibraryEntity.setAnswerCount(correctAnsweredQuestionNumber);
                    userXLibraryEntity.setSchedule(schedule);
                    userXLibraryEntity.setFinishTime(null);
                    updateUserLibraryList.add(userXLibraryEntity);
                }
            }
            if (CollectionUtils.isNotEmpty(updateUserLibraryList)) {
//                userXLibraryMapper.batchUpdate(updateUserLibraryList);
                userXLibraryMapper.batchUpdateNew(updateUserLibraryList);
            }
        }
    }

    private String getMessageContent(Integer questionId, Integer totalCount) {
        StringBuffer sb = new StringBuffer();
        sb.append("更新用户学习记录" + "\n");
        sb.append("题目id：" + questionId + "\n");
        sb.append("更新数量：" + totalCount + "\n");
        sb.append("时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "\n");
        return sb.toString();
    }


}
