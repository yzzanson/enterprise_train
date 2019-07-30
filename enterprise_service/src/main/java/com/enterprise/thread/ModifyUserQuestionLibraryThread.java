package com.enterprise.thread;

import com.enterprise.base.common.SpringContextHolder;
import com.enterprise.base.entity.UserXLibraryEntity;
import com.enterprise.base.enums.StatusEnum;
import com.enterprise.mapper.questions.UserXQuestionsMapper;
import com.enterprise.oa.OAMessageUtil;
import com.enterprise.service.question.QuestionsService;
import com.enterprise.service.question.UserXLibraryService;
import com.enterprise.util.QuestionUtil;
import org.apache.commons.collections.CollectionUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 导入题目后,自动更新用户学习进度
 *
 * @author anson
 * @create 2018-03-09 下午3:57
 **/
public class ModifyUserQuestionLibraryThread implements Runnable {

    private Integer libraryId;

    public ModifyUserQuestionLibraryThread(Integer libraryId) {
        this.libraryId = libraryId;
    }

    @Override
    public void run() {
        Integer totalUpdateCount = 0;
        UserXLibraryService userXLibraryService = SpringContextHolder.getBean(UserXLibraryService.class);
        QuestionsService questionsService = SpringContextHolder.getBean(QuestionsService.class);
        UserXQuestionsMapper userXQuestionMapper = SpringContextHolder.getBean(UserXQuestionsMapper.class);
        //获取所有该题库学习的用户列表
//        List<UserXLibraryEntity> updateList = new ArrayList<>();
        List<UserXLibraryEntity> userLibraryList = userXLibraryService.getByLibraryId(libraryId);
        Integer totalCount = questionsService.getQuestionCount(libraryId);
        if(CollectionUtils.isNotEmpty(userLibraryList)){
            for (int i = 0; i < userLibraryList.size() ; i++) {
                UserXLibraryEntity userXLibraryEntity = userLibraryList.get(i);
                Integer companyId = userXLibraryEntity.getCompanyId();
                Integer userId = userXLibraryEntity.getUserId();
                Integer correctCount = userXQuestionMapper.getUserCorrectQuestionsByLibraryId(companyId, userId, libraryId);
                BigDecimal schedule = new BigDecimal(QuestionUtil.getPercentDivision(correctCount, totalCount));
                userXLibraryEntity.setSchedule(schedule);
                userXLibraryEntity.setAnswerCount(correctCount);
                userXLibraryEntity.setUpdateTime(new Date());
                userXLibraryService.createOrUpdateUserXLibrary(userXLibraryEntity);
            }
        }


        //user_x_question状态为2则为重新学习的题
        Integer batchUpdateResult = userXLibraryService.batchUpdateState(libraryId, StatusEnum.OK.getValue());
        if (batchUpdateResult > 0) {
            String messageContnt = getMessageContent(libraryId, totalUpdateCount);
            OAMessageUtil.sendTextMsgToDept(messageContnt);
        }

    }

    private String getMessageContent(Integer libraryId, Integer totalCount) {
        StringBuffer sb = new StringBuffer();
        sb.append("更新题库修改记录"+"\n");
        sb.append("题库id：" + libraryId + "\n");
        sb.append("更新数量：" + totalCount + "\n");
        sb.append("时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "\n");
        return sb.toString();
    }


}
